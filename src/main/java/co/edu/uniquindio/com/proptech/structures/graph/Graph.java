package co.edu.uniquindio.com.proptech.structures.graph;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import lombok.Getter;

public class Graph<T> {

    @Getter
    private final HashTable<String, GraphNode<T>> nodes;
    private final HashTable<String, ArrayList<GraphEdge<T>>> adjacencyList;

    public Graph() {
        this.nodes = new HashTable<>();
        this.adjacencyList = new HashTable<>();
    }

    public void addNode(GraphNode<T> node) {
        if (nodes.get(node.getId()) == null) {
            nodes.put(node.getId(), node);
            adjacencyList.put(node.getId(), new ArrayList<>());
        }
    }

    public void addEdge(String fromId, String toId, double weight) {
        GraphNode<T> from = nodes.get(fromId);
        GraphNode<T> to   = nodes.get(toId);
        if (from == null || to == null) return;

        addDirectedEdge(fromId, to, weight);
        addDirectedEdge(toId, from, weight);
    }

    private void addDirectedEdge(String fromId, GraphNode<T> to, double weight) {
        ArrayList<GraphEdge<T>> edges = adjacencyList.get(fromId);
        for (int i = 0; i < edges.size(); i++) {
            GraphEdge<T> edge = edges.get(i);
            if (edge.getTarget().getId().equals(to.getId())) {
                edges.set(i, new GraphEdge<>(to, edge.getWeight() + weight));
                return;
            }
        }
        edges.add(new GraphEdge<>(to, weight));
    }

    public void removeNode(String id) {
        nodes.remove(id);
        adjacencyList.remove(id);
        for (ArrayList<GraphEdge<T>> edges : adjacencyList.values()) {
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).getTarget().getId().equals(id)) {
                    edges.remove(i);
                    break;
                }
            }
        }
    }

    public void removeEdge(String fromId, String toId) {
        removeDirectedEdge(fromId, toId);
        removeDirectedEdge(toId, fromId);
    }

    private void removeDirectedEdge(String fromId, String toId) {
        ArrayList<GraphEdge<T>> edges = adjacencyList.get(fromId);
        if (edges == null) return;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getTarget().getId().equals(toId)) {
                edges.remove(i);
                return;
            }
        }
    }

    public double getEdgeWeight(String fromId, String toId) {
        ArrayList<GraphEdge<T>> edges = adjacencyList.get(fromId);
        if (edges == null) return 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getTarget().getId().equals(toId)) {
                return edges.get(i).getWeight();
            }
        }
        return 0;
    }

    public ArrayList<GraphEdge<T>> getNeighbors(String id) {
        return adjacencyList.get(id);
    }

    public GraphNode<T> getNode(String id) {
        return nodes.get(id);
    }

    public boolean containsNode(String id) {
        return nodes.get(id) != null;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }
}