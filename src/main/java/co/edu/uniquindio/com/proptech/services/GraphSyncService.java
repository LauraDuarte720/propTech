package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.enums.InteractionWeight;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.AlgorithmRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.graph.GraphNode;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class GraphSyncService {

    private static final int DOMINANCE_WINDOW_DAYS = 7;
    private static final int DOMINANCE_MIN_COUNT   = 3;

    private final AlgorithmRepository algorithmRepository;
    private final OperationService operationService;

    public GraphSyncService(AlgorithmRepository algorithmRepository,
                            @Lazy OperationService operationService) {
        this.algorithmRepository = algorithmRepository;
        this.operationService = operationService;
    }

    // ══════════════════════════════════════════════
    // PUNTO DE ENTRADA PRINCIPAL
    // ══════════════════════════════════════════════

    public void registerInteractionInGraph(UserInteraction interaction) {
        String clientId   = interaction.getClient().getCedula();
        String propertyId = interaction.getProperty().getCode();
        double weight     = InteractionWeight.of(interaction.getInteractionType());

        Graph<Object> graph = algorithmRepository.getClientPropertyGraph();

        if (!graph.containsNode(clientId)) {
            graph.addNode(new GraphNode<>(clientId, interaction.getClient()));
        }
        if (!graph.containsNode(propertyId)) {
            graph.addNode(new GraphNode<>(propertyId, interaction.getProperty()));
        }

        graph.addEdge(clientId, propertyId, weight);

        Client client = interaction.getClient();
        updateSearchStatus(client);
        registerZoneTransition(interaction);
    }

    // ══════════════════════════════════════════════
    // ACTUALIZACIÓN DE SEARCH STATUS
    // ══════════════════════════════════════════════

    private void updateSearchStatus(Client client) {
        for (Operation operation : operationService.getAllOperations()) {
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

    // ══════════════════════════════════════════════
    // GRAFO DE ZONAS — MOVILIDAD COMERCIAL
    // ══════════════════════════════════════════════

    private void registerZoneTransition(UserInteraction current) {
        Neighborhood to = neighborhoodOf(current);
        if (to == null) return;

        Neighborhood from = findDominantZone(current.getClient(), current.getTimestamp());
        if (from == null) return;

        if (sameZone(from, to)) return;

        if (from.getZone() != null && to.getZone() != null) {
            registerZoneEdge(
                    zoneKey(from), buildZoneNode(from),
                    zoneKey(to),   buildZoneNode(to)
            );
        }

        if (from.getCity() != null && to.getCity() != null
                && !from.getCity().equals(to.getCity())) {
            registerZoneEdge(
                    cityKey(from), buildCityNode(from),
                    cityKey(to),   buildCityNode(to)
            );
        }
    }

    private void registerZoneEdge(String fromKey, ZoneNode fromData,
                                  String toKey,   ZoneNode toData) {
        Graph<GeographicZone> g = algorithmRepository.getZoneGraph();

        if (!g.containsNode(fromKey)) {
            g.addNode(new GraphNode<>(fromKey, toGeographicZone(fromData)));
        }
        if (!g.containsNode(toKey)) {
            g.addNode(new GraphNode<>(toKey, toGeographicZone(toData)));
        }

        g.addDirectedEdge(fromKey, toKey, 1.0);
    }

    // ══════════════════════════════════════════════
    // ZONA DOMINANTE
    // ══════════════════════════════════════════════

    private Neighborhood findDominantZone(Client client, LocalDateTime reference) {
        LocalDateTime windowStart = reference.minusDays(DOMINANCE_WINDOW_DAYS);

        HashTable<String, Integer>      counts          = new HashTable<>();
        HashTable<String, Neighborhood> representatives = new HashTable<>();

        for (InteractionType type : InteractionType.values()) {
            ArrayList<UserInteraction> list = client.getInteractionsByType(type);
            if (list == null) continue;

            for (int i = 0; i < list.size(); i++) {
                UserInteraction ui = list.get(i);
                if (ui.getTimestamp() == null) continue;
                if (!ui.getTimestamp().isBefore(reference)) continue;
                if (ui.getTimestamp().isBefore(windowStart)) continue;

                Neighborhood n = neighborhoodOf(ui);
                if (n == null || n.getCity() == null || n.getZone() == null) continue;

                String key = n.getCity().name() + "|" + n.getZone().name();
                Integer current = counts.get(key);
                counts.put(key, current == null ? 1 : current + 1);

                if (!representatives.containsKey(key)) {
                    representatives.put(key, n);
                }
            }
        }

        String bestKey   = null;
        int    bestCount = DOMINANCE_MIN_COUNT - 1;

        for (String key : counts.keys()) {
            int c = counts.get(key);
            if (c > bestCount) {
                bestCount = c;
                bestKey   = key;
            }
        }

        return bestKey != null ? representatives.get(bestKey) : null;
    }

    // ══════════════════════════════════════════════
    // HELPERS PRIVADOS
    // ══════════════════════════════════════════════

    private Neighborhood neighborhoodOf(UserInteraction i) {
        if (i == null || i.getProperty() == null) return null;
        return i.getProperty().getNeighborhood();
    }

    private boolean sameZone(Neighborhood a, Neighborhood b) {
        return a.getCity() != null && a.getCity().equals(b.getCity())
                && a.getZone() != null && a.getZone().equals(b.getZone());
    }

    private String cityKey(Neighborhood n) {
        return "CITY|" + n.getCity().name();
    }

    private String zoneKey(Neighborhood n) {
        return "ZONE|" + n.getCity().name() + "|" + n.getZone().name();
    }

    private String neighborhoodKey(Neighborhood n) {
        return "NBH|" + n.getCity().name() + "|" + n.getZone().name()
                + "|" + n.getName().toUpperCase();
    }

    private ZoneNode buildCityNode(Neighborhood n) {
        return ZoneNode.builder()
                .level(ZoneNode.Level.CITY)
                .city(n.getCity())
                .build();
    }

    private ZoneNode buildZoneNode(Neighborhood n) {
        return ZoneNode.builder()
                .level(ZoneNode.Level.ZONE)
                .city(n.getCity())
                .zone(n.getZone())
                .build();
    }

    private ZoneNode buildNeighborhoodNode(Neighborhood n) {
        return ZoneNode.builder()
                .level(ZoneNode.Level.NEIGHBORHOOD)
                .city(n.getCity())
                .zone(n.getZone())
                .neighborhoodName(n.getName())
                .build();
    }

    private GeographicZone toGeographicZone(ZoneNode node) {
        return GeographicZone.builder()
                .city(node.getCity())
                .zone(node.getZone())
                .nameNeighborhood(node.getNeighborhoodName())
                .build();
    }
}