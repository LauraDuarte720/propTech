package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.repositories.AlgorithmRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.graph.GraphEdge;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.springframework.stereotype.Service;

/**
 * Responsabilidad única: calcular recomendaciones de propiedades
 * para un cliente y detectar propiedades similares entre sí.
 *
 * Usa scoring basado en presupuesto, zona, tipo, habitaciones,
 * historial personal del cliente en el grafo y comportamiento
 * de clientes similares.
 */
@Service
public class RecommendationService {

    private static final double SIMILAR_CLIENT_WEIGHT_FACTOR = 0.5;

    private final AlgorithmRepository algorithmRepository;
    private final ClientService clientService;
    private final PropertyService propertyService;
    private final ZoneMatcher zoneMatcher;

    public RecommendationService(AlgorithmRepository algorithmRepository,
                                 ClientService clientService,
                                 PropertyService propertyService,
                                 ZoneMatcher zoneMatcher) {
        this.algorithmRepository = algorithmRepository;
        this.clientService = clientService;
        this.propertyService = propertyService;
        this.zoneMatcher = zoneMatcher;
    }

    // ══════════════════════════════════════════════
    // RECOMENDACIÓN PERSONALIZADA
    // ══════════════════════════════════════════════

    /**
     * Retorna las propiedades disponibles ordenadas por puntaje de relevancia
     * para el cliente dado, combinando sus preferencias, historial en el grafo
     * y el comportamiento de clientes similares.
     */
    public ArrayList<Property> recommendProperties(String clientId) {
        Client client = clientService.getClientByCedula(clientId);
        HashTable<String, Property> allProperties = propertyService.getAllProperties();

        HashTable<String, Double> graphWeights      = getClientGraphWeights(clientId);
        HashTable<String, Double> similarClientsBonus = getSimilarClientsBonus(clientId, graphWeights);

        ArrayList<ScoredProperty> scored = new ArrayList<>();

        for (Property property : allProperties.values()) {
            if (!property.isAvailable()) continue;

            double score = 0;

            if (property.getPrice() <= client.getBudget())                      score += 3;
            if (matchesInterestZone(client, property))                           score += 2;
            if (client.getDesiredPropertyType() != null
                    && client.getDesiredPropertyType().equals(property.getPropertyType())) score += 3;
            if (client.getMinBedrooms() != null && property.getNumBedrooms() != null
                    && property.getNumBedrooms() >= client.getMinBedrooms())    score += 2;

            Double graphWeight = graphWeights.get(property.getCode());
            if (graphWeight != null) score += graphWeight;

            Double bonus = similarClientsBonus.get(property.getCode());
            if (bonus != null) score += bonus;

            scored.add(new ScoredProperty(property, score));
        }

        insertionSort(scored);
        return extractProperties(scored);
    }

    // ══════════════════════════════════════════════
    // PROPIEDADES RELACIONADAS CON UN CLIENTE
    // ══════════════════════════════════════════════

    /**
     * Requisito 12: retorna todas las propiedades con las que el cliente
     * tiene al menos una arista en el grafo.
     */
    public ArrayList<Property> getPropertiesRelatedToClient(String clientId) {
        ArrayList<GraphEdge<Object>> edges =
                algorithmRepository.getClientPropertyGraph().getNeighbors(clientId);
        ArrayList<Property> result = new ArrayList<>();
        if (edges == null) return result;

        for (int i = 0; i < edges.size(); i++) {
            Object data = edges.get(i).getTarget().getData();
            if (data instanceof Property) result.add((Property) data);
        }
        return result;
    }

    // ══════════════════════════════════════════════
    // PROPIEDADES SIMILARES (colaborativo por co-visita)
    // ══════════════════════════════════════════════

    /**
     * Retorna propiedades vistas por los mismos clientes que visitaron
     * la propiedad indicada, ordenadas por frecuencia de co-visita.
     */
    public ArrayList<Property> getSimilarProperties(String propertyCode) {
        HashTable<String, Integer> frequency = calculateSimilarPropertyFrequency(propertyCode);
        ArrayList<ScoredProperty> scored     = buildScoredProperties(frequency);
        insertionSort(scored);
        return extractProperties(scored);
    }

    // ══════════════════════════════════════════════
    // MÉTODOS PRIVADOS DE APOYO — GRAFO
    // ══════════════════════════════════════════════

    private HashTable<String, Double> getClientGraphWeights(String clientId) {
        HashTable<String, Double> weights = new HashTable<>();
        ArrayList<GraphEdge<Object>> edges =
                algorithmRepository.getClientPropertyGraph().getNeighbors(clientId);
        if (edges == null) return weights;

        for (int i = 0; i < edges.size(); i++) {
            GraphEdge<Object> edge = edges.get(i);
            if (edge.getTarget().getData() instanceof Property) {
                weights.put(edge.getTarget().getId(), edge.getWeight());
            }
        }
        return weights;
    }

    private HashTable<String, Double> getSimilarClientsBonus(String clientId,
                                                             HashTable<String, Double> ownWeights) {
        HashTable<String, Double> bonus = new HashTable<>();
        ArrayList<Client> similarClients = getClientsWithSharedProperties(clientId);

        for (int i = 0; i < similarClients.size(); i++) {
            String similarId = similarClients.get(i).getCedula();
            ArrayList<GraphEdge<Object>> edges =
                    algorithmRepository.getClientPropertyGraph().getNeighbors(similarId);
            if (edges == null) continue;

            for (int j = 0; j < edges.size(); j++) {
                Object data = edges.get(j).getTarget().getData();
                if (!(data instanceof Property)) continue;

                String code = edges.get(j).getTarget().getId();
                if (ownWeights.get(code) != null) continue; // ya la conoce el cliente

                double contribution = edges.get(j).getWeight() * SIMILAR_CLIENT_WEIGHT_FACTOR;
                Double current = bonus.get(code);
                bonus.put(code, current == null ? contribution : current + contribution);
            }
        }
        return bonus;
    }

    private ArrayList<Client> getClientsWithSharedProperties(String clientId) {
        ArrayList<GraphEdge<Object>> clientEdges =
                algorithmRepository.getClientPropertyGraph().getNeighbors(clientId);
        if (clientEdges == null) return new ArrayList<>();

        ArrayList<Client> result = new ArrayList<>();

        for (int i = 0; i < clientEdges.size(); i++) {
            Object data = clientEdges.get(i).getTarget().getData();
            if (!(data instanceof Property)) continue;

            Property property = (Property) data;
            ArrayList<GraphEdge<Object>> propertyEdges =
                    algorithmRepository.getClientPropertyGraph().getNeighbors(property.getCode());
            if (propertyEdges == null) continue;

            for (int j = 0; j < propertyEdges.size(); j++) {
                Object neighborData = propertyEdges.get(j).getTarget().getData();
                if (neighborData instanceof Client) {
                    Client neighbor = (Client) neighborData;
                    if (!neighbor.getCedula().equals(clientId)) result.add(neighbor);
                }
            }
        }
        return result;
    }

    // ── helpers para propiedades similares ───────────────────────────────────

    private HashTable<String, Integer> calculateSimilarPropertyFrequency(String propertyCode) {
        HashTable<String, Integer> frequency = new HashTable<>();

        ArrayList<GraphEdge<Object>> propertyEdges =
                algorithmRepository.getClientPropertyGraph().getNeighbors(propertyCode);
        if (propertyEdges == null) return frequency;

        for (int i = 0; i < propertyEdges.size(); i++) {
            Object clientData = propertyEdges.get(i).getTarget().getData();
            if (!(clientData instanceof Client)) continue;

            addClientRelatedProperties((Client) clientData, propertyCode, frequency);
        }

        return frequency;
    }

    private void addClientRelatedProperties(Client client,
                                            String originalPropertyCode,
                                            HashTable<String, Integer> frequency) {
        ArrayList<GraphEdge<Object>> clientEdges =
                algorithmRepository.getClientPropertyGraph().getNeighbors(client.getCedula());
        if (clientEdges == null) return;

        for (int i = 0; i < clientEdges.size(); i++) {
            Object propertyData = clientEdges.get(i).getTarget().getData();
            if (!(propertyData instanceof Property)) continue;

            Property property = (Property) propertyData;
            if (property.getCode().equals(originalPropertyCode)) continue;

            Integer current = frequency.get(property.getCode());
            frequency.put(property.getCode(), current == null ? 1 : current + 1);
        }
    }

    private ArrayList<ScoredProperty> buildScoredProperties(HashTable<String, Integer> frequency) {
        ArrayList<ScoredProperty> scored = new ArrayList<>();
        for (String code : frequency.keys()) {
            Property property = propertyService.getPropertyByCode(code);
            if (property != null) {
                scored.add(new ScoredProperty(property, frequency.get(code)));
            }
        }
        return scored;
    }

    // ── utilidades ────────────────────────────────────────────────────────────

    private boolean matchesInterestZone(Client client, Property property) {
        if (client.getInterestZones() == null || property.getNeighborhood() == null) return false;
        for (int i = 0; i < client.getInterestZones().size(); i++) {
            if (zoneMatcher.match(client.getInterestZones().get(i), property.getNeighborhood()))
                return true;
        }
        return false;
    }

    private ArrayList<Property> extractProperties(ArrayList<ScoredProperty> scored) {
        ArrayList<Property> result = new ArrayList<>();
        for (int i = 0; i < scored.size(); i++) {
            result.add(scored.get(i).property);
        }
        return result;
    }

    /** Insertion sort descendente por puntaje. */
    private void insertionSort(ArrayList<ScoredProperty> list) {
        for (int i = 1; i < list.size(); i++) {
            ScoredProperty key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).score < key.score) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ── clase interna de apoyo ────────────────────────────────────────────────

    private static class ScoredProperty {
        final Property property;
        final double   score;

        ScoredProperty(Property property, double score) {
            this.property = property;
            this.score    = score;
        }
    }

    /**
     * Retorna una cola de prioridad de clientes ordenados por su intención
     * de cierre, calculada sumando sus interacciones de tipo
     * BUYING_INTENTION y RENTING_INTENTION en el historial.
     */
    public PriorityQueue<Client> getClientsByClosingIntention() {
        HashTable<String, Client> allClients = clientService.getClients();
        // Comparator: mayor intención = mayor prioridad
        PriorityQueue<Client> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(closingScore(b), closingScore(a))
        );
        for (Client client : allClients.values()) {
            if (closingScore(client) > 0) {
                queue.add(client);
            }
        }
        return queue;
    }

    private int closingScore(Client client) {
        int score = 0;
        ArrayList<UserInteraction> buying =
                client.getInteractionsByType(InteractionType.BUYING_INTENTION);
        ArrayList<UserInteraction> renting =
                client.getInteractionsByType(InteractionType.RENTING_INTENTION);
        if (buying  != null) score += buying.size();
        if (renting != null) score += renting.size();
        return score;
    }

// ══════════════════════════════════════════════
// COLA DE PRIORIDAD — INMUEBLES CON MAYOR DEMANDA
// ══════════════════════════════════════════════

    /**
     * Retorna una cola de prioridad de propiedades ordenadas por demanda,
     * calculada sumando sus interacciones de tipo VISITED, SAVED y
     * NEGOTIATED en el grafo cliente-propiedad.
     */
    public PriorityQueue<Property> getPropertiesByDemand() {
        HashTable<String, Property> allProperties = propertyService.getAllProperties();
        HashTable<String, Integer> frequency = buildPropertyDemandFrequency();

        // Comparator: mayor frecuencia = mayor prioridad
        PriorityQueue<Property> queue = new PriorityQueue<>(
                (a, b) -> {
                    int fa = frequency.get(a.getCode()) != null ? frequency.get(a.getCode()) : 0;
                    int fb = frequency.get(b.getCode()) != null ? frequency.get(b.getCode()) : 0;
                    return Integer.compare(fb, fa);
                }
        );

        for (Property property : allProperties.values()) {
            queue.add(property);
        }
        return queue;
    }

    private HashTable<String, Integer> buildPropertyDemandFrequency() {
        HashTable<String, Client> allClients = clientService.getClients();
        HashTable<String, Integer> frequency = new HashTable<>();

        InteractionType[] demandTypes = {
                InteractionType.VISITED,
                InteractionType.SAVED,
                InteractionType.NEGOTIATED
        };

        for (Client client : allClients.values()) {
            for (InteractionType type : demandTypes) {
                ArrayList<UserInteraction> interactions = client.getInteractionsByType(type);
                if (interactions == null) continue;
                for (int i = 0; i < interactions.size(); i++) {
                    Property p = interactions.get(i).getProperty();
                    if (p == null) continue;
                    Integer current = frequency.get(p.getCode());
                    frequency.put(p.getCode(), current == null ? 1 : current + 1);
                }
            }
        }
        return frequency;
    }
}