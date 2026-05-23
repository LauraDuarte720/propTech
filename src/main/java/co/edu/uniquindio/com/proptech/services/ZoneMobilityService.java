package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.AlgorithmRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.graph.GraphEdge;
import co.edu.uniquindio.com.proptech.structures.graph.GraphNode;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.stereotype.Service;

/**
 * Responsabilidad única: exponer consultas analíticas sobre el
 * grafo de zonas geográficas (movilidad comercial de clientes).
 *
 * Los datos los escribe GraphSyncService; este servicio solo lee.
 */
@Service
public class ZoneMobilityService {

    private static final int DOMINANCE_MIN_COUNT = 3;

    private final AlgorithmRepository algorithmRepository;

    public ZoneMobilityService(AlgorithmRepository algorithmRepository) {
        this.algorithmRepository = algorithmRepository;
    }

    // ══════════════════════════════════════════════
    // CONSULTAS PÚBLICAS
    // ══════════════════════════════════════════════

    /**
     * Retorna los patrones de movilidad salientes desde una zona dada.
     * Ejemplo: desde "ZONE|ARMENIA|SUR" → hacia dónde se mueven los clientes
     * y con qué frecuencia acumulada.
     *
     * @param zoneKey clave del nodo en el grafo (ej. "ZONE|ARMENIA|SUR",
     *                "CITY|BOGOTA", "NBH|ARMENIA|SUR|LAURELES")
     */
    public ArrayList<ZoneTransitionPattern> getMobilityPatternsFrom(String zoneKey) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(zoneKey);
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        if (edges == null) return result;

        GraphNode<GeographicZone> fromNode = g.getNode(zoneKey);
        if (fromNode == null) return result;

        for (int i = 0; i < edges.size(); i++) {
            GraphEdge<GeographicZone> edge = edges.get(i);
            result.add(ZoneTransitionPattern.builder()
                    .from(toZoneNode(fromNode.getData()))
                    .to(toZoneNode(edge.getTarget().getData()))
                    .weight(edge.getWeight())
                    .build());
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getMobilityPatternsTo(String zoneKey) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(zoneKey);
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        if (incoming.isEmpty()) return result;

        GraphNode<GeographicZone> toNode = g.getNode(zoneKey);
        if (toNode == null) return result;

        for (int i = 0; i < incoming.size(); i++) {
            GraphEdge<GeographicZone> edge = incoming.get(i);
            result.add(ZoneTransitionPattern.builder()
                    .from(toZoneNode(edge.getTarget().getData()))
                    .to(toZoneNode(toNode.getData()))
                    .weight(edge.getWeight())
                    .build());
        }

        sortByWeightDesc(result);
        return result;
    }

    /**
     * Retorna el ranking de zonas destino más frecuentes en todo el sistema.
     * Útil para detectar zonas con mayor atracción comercial.
     */
    public ArrayList<ZoneTransitionPattern> getTopDestinationZones() {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(node.getId());
            if (incoming.isEmpty()) continue;
            double total = 0;
            for (int i = 0; i < incoming.size(); i++) {
                total += incoming.get(i).getWeight();
            }
            result.add(ZoneTransitionPattern.builder()
                    .from(null)
                    .to(toZoneNode(node.getData()))
                    .weight(total)
                    .build());
        }
        sortByWeightDesc(result);
        return result;
    }


    // ══════════════════════════════════════════════
    // HELPERS PRIVADOS
    // ══════════════════════════════════════════════

    private ZoneNode toZoneNode(GeographicZone gz) {
        if (gz == null) return null;
        if (gz.getNameNeighborhood() != null) {
            return ZoneNode.builder()
                    .level(ZoneNode.Level.NEIGHBORHOOD)
                    .city(gz.getCity())
                    .zone(gz.getZone())
                    .neighborhoodName(gz.getNameNeighborhood())
                    .build();
        }
        if (gz.getZone() != null) {
            return ZoneNode.builder()
                    .level(ZoneNode.Level.ZONE)
                    .city(gz.getCity())
                    .zone(gz.getZone())
                    .build();
        }
        return ZoneNode.builder()
                .level(ZoneNode.Level.CITY)
                .city(gz.getCity())
                .build();
    }

    /** Insertion sort descendente por peso. */
    private void sortByWeightDesc(ArrayList<ZoneTransitionPattern> list) {
        for (int i = 1; i < list.size(); i++) {
            ZoneTransitionPattern key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getWeight() < key.getWeight()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}