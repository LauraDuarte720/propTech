package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.enums.InteractionWeight;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.ClientDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.graph.GraphEdge;
import co.edu.uniquindio.com.proptech.structures.graph.GraphNode;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AlgorithmService {

    private final PropTech propTech;
    private final ClientRepository clientRepository;
    private final PropertyService propertyService;
    private final ZoneMatcher zoneMatcher;

    public AlgorithmService(PropTech propTech, ClientRepository clientRepository, PropertyService propertyService, ZoneMatcher zoneMatcher) {
        this.propTech = propTech;
        this.clientRepository = clientRepository;
        this.propertyService = propertyService;
        this.zoneMatcher = zoneMatcher;
    }

    // ══════════════════════════════════════════════
    // SINCRONIZACIÓN DEL GRAFO
    // ══════════════════════════════════════════════

    // Llamar desde ClientService.registerUserInteraction()
    public void registerInteractionInGraph(UserInteraction interaction) {
        String clientId   = interaction.getClient().getCedula();
        String propertyId = interaction.getProperty().getCode();
        double weight     = InteractionWeight.of(interaction.getInteractionType());

        // Agregar nodos si no existen
        if (!propTech.getClientPropertyGraph().containsNode(clientId)) {
            propTech.getClientPropertyGraph().addNode(
                    new GraphNode<>(clientId, interaction.getClient())
            );
        }
        if (!propTech.getClientPropertyGraph().containsNode(propertyId)) {
            propTech.getClientPropertyGraph().addNode(
                    new GraphNode<>(propertyId, interaction.getProperty())
            );
        }

        // Agregar/actualizar arista con el peso de la interacción
        Client client = (Client) propTech.getClientPropertyGraph()
                .getNode(interaction.getClient().getCedula()).getData();
        updateSearchStatus(client);
    }

    // Llamar desde AgentService cuando se registra una operación entre zonas
    public void registerZoneConnection(GeographicZone zoneA, GeographicZone zoneB, double weight) {
        if (!propTech.getZoneGraph().containsNode(zoneA.getId())) {
            propTech.getZoneGraph().addNode(new GraphNode<>(zoneA.getId(), zoneA));
        }
        if (!propTech.getZoneGraph().containsNode(zoneB.getId())) {
            propTech.getZoneGraph().addNode(new GraphNode<>(zoneB.getId(), zoneB));
        }
        propTech.getZoneGraph().addEdge(zoneA.getId(), zoneB.getId(), weight);
    }

    // ══════════════════════════════════════════════
    // ALGORITMO DE RECOMENDACIÓN CON SCORING
    // ══════════════════════════════════════════════

    public ArrayList<Property> recommendProperties(String clientId) {
        Client client = clientRepository.findByCedula(clientId)
                .orElseThrow(() -> new ClientDoesNotExist("cedula", clientId));
        HashTable<String, Property> allProperties = propertyService.getAllProperties();

        // Paso 1 — obtener peso del grafo para cada propiedad (historial del cliente)
        HashTable<String, Double> graphWeights = getClientGraphWeights(clientId);

        // Paso 2 — obtener propiedades visitadas por clientes similares
        HashTable<String, Integer> similarClientsBonus = getSimilarClientsBonus(clientId);

        // Paso 3 — calcular puntaje para cada propiedad
        ArrayList<ScoredProperty> scored = new ArrayList<>();
        for (Property property : allProperties.values()) {
            if (!property.isAvailable()) continue;

            double score = 0;

            // Presupuesto (+3 si está dentro del presupuesto)
            if (property.getPrice() <= client.getBudget()) score += 3;

            // Zona de interés (+2 si coincide con alguna zona de interés)
            if (matchesInterestZone(client,property)) score += 2;

            // Tipo de inmueble (+2 si coincide)
            if (client.getDesiredPropertyType() != null &&
                    client.getDesiredPropertyType().equals(property.getPropertyType())) score += 2;

            // Habitaciones (+2 si cumple el mínimo)
            if (client.getMinBedrooms() != null && property.getNumBedrooms() != null &&
                    property.getNumBedrooms() >= client.getMinBedrooms()) score += 2;

            // Historial del cliente en el grafo (peso acumulado de interacciones)
            Double graphWeight = graphWeights.get(property.getCode());
            if (graphWeight != null) score += graphWeight;

            // Bonus por clientes similares que la visitaron
            Integer bonus = similarClientsBonus.get(property.getCode());
            if (bonus != null) score += bonus;

            scored.add(new ScoredProperty(property, score));
        }

        // Paso 4 — ordenar por puntaje de mayor a menor (insertion sort)
        insertionSort(scored);

        // Paso 5 — retornar solo las propiedades ordenadas
        ArrayList<Property> result = new ArrayList<>();
        for (int i = 0; i < scored.size(); i++) {
            result.add(scored.get(i).property);
        }
        return result;
    }

    // ══════════════════════════════════════════════
    // ANÁLISIS ESTRUCTURAL DEL GRAFO
    // ══════════════════════════════════════════════

    // Requisito 12: consultar relaciones cliente ↔ inmueble
    public ArrayList<Property> getPropertiesRelatedToClient(String clientId) {
        ArrayList<GraphEdge<Object>> edges =
                propTech.getClientPropertyGraph().getNeighbors(clientId);
        ArrayList<Property> result = new ArrayList<>();
        if (edges == null) return result;

        for (int i = 0; i < edges.size(); i++) {
            Object data = edges.get(i).getTarget().getData();
            if (data instanceof Property) {
                result.add((Property) data);
            }
        }
        return result;
    }

    // Detectar propiedades similares consultadas por múltiples clientes
    public ArrayList<Client> getClientsWithSharedProperties(String clientId) {
        ArrayList<GraphEdge<Object>> clientEdges =
                propTech.getClientPropertyGraph().getNeighbors(clientId);
        if (clientEdges == null) return new ArrayList<>();

        ArrayList<Client> result = new ArrayList<>();

        for (int i = 0; i < clientEdges.size(); i++) {
            Object data = clientEdges.get(i).getTarget().getData();
            if (!(data instanceof Property)) continue;

            Property property = (Property) data;
            ArrayList<GraphEdge<Object>> propertyEdges =
                    propTech.getClientPropertyGraph().getNeighbors(property.getCode());
            if (propertyEdges == null) continue;

            for (int j = 0; j < propertyEdges.size(); j++) {
                Object neighborData = propertyEdges.get(j).getTarget().getData();
                if (neighborData instanceof Client) {
                    Client neighbor = (Client) neighborData;
                    if (!neighbor.getCedula().equals(clientId)) {
                        result.add(neighbor);
                    }
                }
            }
        }
        return result;
    }

    // Zona más activa comercialmente
    public GeographicZone getMostActiveZone() {
        GeographicZone mostActive = null;
        int maxConnections = -1;

        for (GraphNode<GeographicZone> node : propTech.getZoneGraph().getNodes().values()) {
            ArrayList<GraphEdge<GeographicZone>> edges =
                    propTech.getZoneGraph().getNeighbors(node.getId());
            int connections = edges == null ? 0 : edges.size();
            if (connections > maxConnections) {
                maxConnections = connections;
                mostActive = node.getData();
            }
        }
        return mostActive;
    }

    // ══════════════════════════════════════════════
    // MÉTODOS PRIVADOS DE APOYO
    // ══════════════════════════════════════════════

    private HashTable<String, Double> getClientGraphWeights(String clientId) {
        HashTable<String, Double> weights = new HashTable<>();
        ArrayList<GraphEdge<Object>> edges =
                propTech.getClientPropertyGraph().getNeighbors(clientId);
        if (edges == null) return weights;

        for (int i = 0; i < edges.size(); i++) {
            GraphEdge<Object> edge = edges.get(i);
            if (edge.getTarget().getData() instanceof Property) {
                weights.put(edge.getTarget().getId(), edge.getWeight());
            }
        }
        return weights;
    }

    private HashTable<String, Integer> getSimilarClientsBonus(String clientId) {
        HashTable<String, Integer> bonus = new HashTable<>();
        ArrayList<Client> similarClients = getClientsWithSharedProperties(clientId);

        for (int i = 0; i < similarClients.size(); i++) {
            String similarId = similarClients.get(i).getCedula();
            ArrayList<GraphEdge<Object>> edges =
                    propTech.getClientPropertyGraph().getNeighbors(similarId);
            if (edges == null) continue;

            for (int j = 0; j < edges.size(); j++) {
                Object data = edges.get(j).getTarget().getData();
                if (data instanceof Property) {
                    String code = edges.get(j).getTarget().getId();
                    Integer current = bonus.get(code);
                    bonus.put(code, current == null ? 1 : current + 1);
                }
            }
        }
        return bonus;
    }

    private boolean matchesInterestZone(Client client, Property property) {
        if (client.getInterestZones() == null || property.getNeighborhood() == null) return false;
        for (int i = 0; i < client.getInterestZones().size(); i++) {
            GeographicZone zone = client.getInterestZones().get(i);
            if( zoneMatcher.match(zone, property.getNeighborhood()) ) return true;
        }
        return false;
    }

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

    // Clase interna de apoyo para el scoring
    private static class ScoredProperty {
        Property property;
        double score;

        ScoredProperty(Property property, double score) {
            this.property = property;
            this.score = score;
        }
    }

    private void updateSearchStatus(Client client) {
        // 1. Check for active operation (CREATED = in progress)
        for (Operation operation : propTech.getOperations()) {
            if (!operation.getClient().getCedula().equals(client.getCedula())) continue;

            if (operation.getProcessStatus() == ProcessStatus.CREATED) {
                client.setSearchStatus(SearchStatus.NEGOTIATING);
                return;
            }
            if (operation.getProcessStatus() == ProcessStatus.CLOSED) {
                client.setSearchStatus(SearchStatus.CLOSED);
                return;
            }
        }

        // 2. Check interaction recency
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime mostRecent = null;

        for (InteractionType type : InteractionType.values()) {
            ArrayList<UserInteraction> list = client.getInteractionsByType(type);
            if (list == null) continue;
            for (int i = 0; i < list.size(); i++) {
                LocalDateTime ts = list.get(i).getTimestamp();
                if (mostRecent == null || ts.isAfter(mostRecent)) {
                    mostRecent = ts;
                }
            }
        }

        if (mostRecent == null) {
            client.setSearchStatus(SearchStatus.INACTIVE);
            return;
        }

        long daysSinceLast = ChronoUnit.DAYS.between(mostRecent, now);

        if (daysSinceLast <= 30) {
            client.setSearchStatus(SearchStatus.ACTIVE);
        } else if (daysSinceLast <= 90) {
            client.setSearchStatus(SearchStatus.PAUSED);
        } else {
            client.setSearchStatus(SearchStatus.INACTIVE);
        }
    }
}