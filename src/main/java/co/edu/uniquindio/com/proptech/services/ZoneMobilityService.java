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

@Service
public class ZoneMobilityService {

    private final AlgorithmRepository algorithmRepository;
    private final OperationService operationService;

    public ZoneMobilityService(AlgorithmRepository algorithmRepository,
                               OperationService operationService) {
        this.algorithmRepository = algorithmRepository;
        this.operationService = operationService;
    }

    // ══════════════════════════════════════════════
    // CONSULTAS PÚBLICAS
    // ══════════════════════════════════════════════

    public ArrayList<ZoneTransitionPattern> getMobilityPatternsFrom(String zoneKey) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(zoneKey);
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        if (edges == null) return result;

        GraphNode<GeographicZone> fromNode = g.getNode(zoneKey);
        if (fromNode == null) return result;

        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        for (int i = 0; i < edges.size(); i++) {
            result.add(buildPattern(fromNode, edges.get(i), opCounts));
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

        HashTable<String, Integer> opCounts = buildOperationCountByZone();
        ZoneNode to = toZoneNode(toNode.getData());

        for (int i = 0; i < incoming.size(); i++) {
            result.add(buildIncomingPattern(incoming.get(i), to, opCounts));
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getTopDestinationZones() {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(node.getId());
            if (incoming.isEmpty()) continue;

            double total = 0;
            for (int i = 0; i < incoming.size(); i++) {
                total += incoming.get(i).getWeight();
            }

            ZoneNode to = toZoneNode(node.getData());
            result.add(ZoneTransitionPattern.builder()
                    .from(null)
                    .to(to)
                    .weight(total)
                    .operationCount(getOperationCount(opCounts, to))
                    .build());
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getAllMobilityPatterns() {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(node.getId());
            if (edges == null) continue;

            GraphNode<GeographicZone> fromNode = g.getNode(node.getId());
            for (int i = 0; i < edges.size(); i++) {
                result.add(buildPattern(fromNode, edges.get(i), opCounts));
            }
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getOperationCorrelatedPatterns() {
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        ArrayList<ZoneTransitionPattern> allPatterns = getAllMobilityPatterns();

        for (int i = 0; i < allPatterns.size(); i++) {
            ZoneTransitionPattern pattern = allPatterns.get(i);
            if (pattern.getOperationCount() == 0) continue;
            result.add(pattern);
        }

        sortByWeightDesc(result);
        return result;
    }

    // ══════════════════════════════════════════════
    // HELPERS PRIVADOS
    // ══════════════════════════════════════════════

    private HashTable<String, Integer> buildOperationCountByZone() {
        LinkedList<Operation> operations = operationService.getAllOperations();
        HashTable<String, Integer> operationCountByZone = new HashTable<>();

        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            if (op.getProcessStatus() != ProcessStatus.CLOSED) continue;
            if (op.getOperationType() != OperationType.RENT
                    && op.getOperationType() != OperationType.SALE) continue;
            Neighborhood n = op.getProperty().getNeighborhood();
            if (n == null || n.getCity() == null || n.getZone() == null) continue;
            String key = "ZONE|" + n.getCity().name() + "|" + n.getZone().name();
            Integer current = operationCountByZone.get(key);
            operationCountByZone.put(key, current == null ? 1 : current + 1);
        }

        return operationCountByZone;
    }

    private int getOperationCount(HashTable<String, Integer> opCounts, ZoneNode to) {
        if (to == null || to.getCity() == null || to.getZone() == null) return 0;
        String key = "ZONE|" + to.getCity().name() + "|" + to.getZone().name();
        Integer count = opCounts.get(key);
        return count == null ? 0 : count;
    }

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

    private ZoneTransitionPattern buildPattern(GraphNode<GeographicZone> fromNode,
                                               GraphEdge<GeographicZone> edge,
                                               HashTable<String, Integer> opCounts) {
        ZoneNode to = toZoneNode(edge.getTarget().getData());
        return ZoneTransitionPattern.builder()
                .from(toZoneNode(fromNode.getData()))
                .to(to)
                .weight(edge.getWeight())
                .operationCount(getOperationCount(opCounts, to))
                .build();
    }

    private ZoneTransitionPattern buildIncomingPattern(GraphEdge<GeographicZone> edge,
                                                       ZoneNode to,
                                                       HashTable<String, Integer> opCounts) {
        return ZoneTransitionPattern.builder()
                .from(toZoneNode(edge.getTarget().getData()))
                .to(to)
                .weight(edge.getWeight())
                .operationCount(getOperationCount(opCounts, to))
                .build();
    }

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