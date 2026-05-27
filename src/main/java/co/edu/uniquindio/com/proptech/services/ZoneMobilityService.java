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

    public ArrayList<ZoneTransitionPattern> getAllMobilityPatterns() {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(node.getId());
            if (edges == null) continue;
            for (int i = 0; i < edges.size(); i++) {
                result.add(buildPattern(node, edges.get(i), opCounts));
            }
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getAllMobilityPatternsByLevel(String level) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();
        String prefix = level + "|";

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            if (!node.getId().startsWith(prefix)) continue;
            ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(node.getId());
            if (edges == null) continue;
            for (int i = 0; i < edges.size(); i++) {
                if (!edges.get(i).getTarget().getId().startsWith(prefix)) continue;
                result.add(buildPattern(node, edges.get(i), opCounts));
            }
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getOperationCorrelatedPatterns() {
        return filterCorrelated(getAllMobilityPatterns());
    }

    public ArrayList<ZoneTransitionPattern> getOperationCorrelatedPatternsByLevel(String level) {
        return filterCorrelated(getAllMobilityPatternsByLevel(level));
    }

    public ArrayList<ZoneTransitionPattern> getTopDestinationZones() {
        return buildTopDestinations(null);
    }

    public ArrayList<ZoneTransitionPattern> getTopDestinationZonesByLevel(String level) {
        return buildTopDestinations(level);
    }

    public ArrayList<ZoneTransitionPattern> getMobilityPatternsFrom(String zoneKey) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        if (zoneKey.startsWith("CITY|")) {
            String cityName = zoneKey.substring(5);
            for (GraphNode<GeographicZone> node : g.getNodes().values()) {
                if (!node.getId().startsWith("CITY|")) continue;
                GeographicZone gz = node.getData();
                if (gz.getCity() == null) continue;
                if (!gz.getCity().name().equalsIgnoreCase(cityName)) continue;
                ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(node.getId());
                if (edges == null) continue;
                for (int i = 0; i < edges.size(); i++) {
                    result.add(buildPattern(node, edges.get(i), opCounts));
                }
            }
        } else {
            GraphNode<GeographicZone> fromNode = g.getNode(zoneKey);
            if (fromNode == null) return result;
            ArrayList<GraphEdge<GeographicZone>> edges = g.getNeighbors(zoneKey);
            if (edges == null) return result;
            for (int i = 0; i < edges.size(); i++) {
                result.add(buildPattern(fromNode, edges.get(i), opCounts));
            }
        }

        sortByWeightDesc(result);
        return result;
    }

    public ArrayList<ZoneTransitionPattern> getMobilityPatternsTo(String zoneKey) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();

        if (zoneKey.startsWith("CITY|")) {
            String cityName = zoneKey.substring(5);
            for (GraphNode<GeographicZone> node : g.getNodes().values()) {
                if (!node.getId().startsWith("CITY|")) continue;
                GeographicZone gz = node.getData();
                if (gz.getCity() == null) continue;
                if (!gz.getCity().name().equalsIgnoreCase(cityName)) continue;
                ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(node.getId());
                if (incoming == null || incoming.isEmpty()) continue;
                ZoneNode to = toZoneNodeFromId(node.getId(), gz);
                for (int i = 0; i < incoming.size(); i++) {
                    result.add(buildIncomingPattern(incoming.get(i), to, opCounts));
                }
            }
        } else {
            GraphNode<GeographicZone> toNode = g.getNode(zoneKey);
            if (toNode == null) return result;
            ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(zoneKey);
            if (incoming == null || incoming.isEmpty()) return result;
            ZoneNode to = toZoneNodeFromId(toNode.getId(), toNode.getData());
            for (int i = 0; i < incoming.size(); i++) {
                result.add(buildIncomingPattern(incoming.get(i), to, opCounts));
            }
        }

        sortByWeightDesc(result);
        return result;
    }

    // ══════════════════════════════════════════════
    // HELPERS PRIVADOS
    // ══════════════════════════════════════════════

    private ArrayList<ZoneTransitionPattern> filterCorrelated(ArrayList<ZoneTransitionPattern> source) {
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).getOperationCount() > 0) result.add(source.get(i));
        }
        sortByOperationCountDesc(result);
        return result;
    }

    // level == null → todos los nodos sin filtrar por prefijo
    private ArrayList<ZoneTransitionPattern> buildTopDestinations(String level) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();
        ArrayList<ZoneTransitionPattern> result = new ArrayList<>();
        HashTable<String, Integer> opCounts = buildOperationCountByZone();
        String prefix = level != null ? level + "|" : null;

        for (GraphNode<GeographicZone> node : g.getNodes().values()) {
            if (prefix != null && !node.getId().startsWith(prefix)) continue;
            ArrayList<GraphEdge<GeographicZone>> incoming = g.getIncomingEdges(node.getId());
            if (incoming == null || incoming.isEmpty()) continue;

            double total = 0;
            for (int i = 0; i < incoming.size(); i++) {
                if (prefix != null && !incoming.get(i).getTarget().getId().startsWith(prefix)) continue;
                total += incoming.get(i).getWeight();
            }
            if (total == 0) continue;

            ZoneNode to = toZoneNodeFromId(node.getId(), node.getData());
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

    private ZoneTransitionPattern buildPattern(GraphNode<GeographicZone> fromNode,
                                               GraphEdge<GeographicZone> edge,
                                               HashTable<String, Integer> opCounts) {
        ZoneNode from = toZoneNodeFromId(fromNode.getId(), fromNode.getData());
        ZoneNode to   = toZoneNodeFromId(edge.getTarget().getId(), edge.getTarget().getData());
        return ZoneTransitionPattern.builder()
                .from(from)
                .to(to)
                .weight(edge.getWeight())
                .operationCount(getOperationCount(opCounts, to))
                .build();
    }

    private ZoneTransitionPattern buildIncomingPattern(GraphEdge<GeographicZone> edge,
                                                       ZoneNode to,
                                                       HashTable<String, Integer> opCounts) {
        ZoneNode from = toZoneNodeFromId(edge.getTarget().getId(), edge.getTarget().getData());
        return ZoneTransitionPattern.builder()
                .from(from)
                .to(to)
                .weight(edge.getWeight())
                .operationCount(getOperationCount(opCounts, from))
                .build();
    }

    private ZoneNode toZoneNodeFromId(String nodeId, GeographicZone gz) {
        if (gz == null) return null;
        if (nodeId.startsWith("CITY|")) {
            return ZoneNode.builder()
                    .level(ZoneNode.Level.CITY)
                    .city(gz.getCity())
                    .build();
        }
        return ZoneNode.builder()
                .level(ZoneNode.Level.ZONE)
                .city(gz.getCity())
                .zone(gz.getZone())
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

    private void sortByOperationCountDesc(ArrayList<ZoneTransitionPattern> list) {
        for (int i = 1; i < list.size(); i++) {
            ZoneTransitionPattern key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).getOperationCount() < key.getOperationCount()) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}