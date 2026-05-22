package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.ZoneNode;
import co.edu.uniquindio.com.proptech.domain.model.ZoneTransitionPattern;
import co.edu.uniquindio.com.proptech.repositories.AlgorithmRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.graph.GraphEdge;
import co.edu.uniquindio.com.proptech.structures.graph.GraphNode;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.stereotype.Service;

/**
 * Responsabilidad única: exponer consultas analíticas sobre el
 * grafo de zonas geográficas (movilidad comercial de clientes).
 *
 * Los datos los escribe GraphSyncService; este servicio solo lee.
 */
@Service
public class ZoneMobilityService {

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

    /**
     * Retorna el ranking de zonas destino más frecuentes en todo el sistema.
     * Útil para detectar zonas con mayor atracción comercial.
     */
    public ArrayList<ZoneTransitionPattern> getTopDestinationZones() {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        HashTable<String, Double> incomingWeight = new HashTable<>();

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(node.getId());
            if (edges == null) continue;
            for (int i = 0; i < edges.size(); i++) {
                String targetId = edges.get(i).getTarget().getId();
                Double current  = incomingWeight.get(targetId);
                incomingWeight.put(targetId,
                        (current == null ? 0 : current) + edges.get(i).getWeight());
            }
        }

        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        for (String key : incomingWeight.keys()) {
            GraphNode<GeographicZone> node = g.getNode(key);
            if (node == null) continue;
            result.add(ZoneTransitionPattern.builder()
                    .from(null)   // ranking global, sin origen único
                    .to(toZoneNode(node.getData()))
                    .weight(incomingWeight.get(key))
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