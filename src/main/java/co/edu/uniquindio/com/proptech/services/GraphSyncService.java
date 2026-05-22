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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Responsabilidad única: mantener sincronizados los dos grafos
 * (cliente↔propiedad y zonas) a partir de las interacciones del usuario.
 *
 * Se invoca desde ClientService.registerUserInteraction().
 */
@Service
public class GraphSyncService {

    private final AlgorithmRepository algorithmRepository;
    private final OperationService operationService;

    public GraphSyncService(AlgorithmRepository algorithmRepository,
                            OperationService operationService) {
        this.algorithmRepository = algorithmRepository;
        this.operationService = operationService;
    }

    // ══════════════════════════════════════════════
    // PUNTO DE ENTRADA PRINCIPAL
    // ══════════════════════════════════════════════

    /**
     * Registra una interacción en el grafo cliente↔propiedad,
     * actualiza el SearchStatus del cliente y registra la transición de zona.
     *
     * Llamar desde ClientService.registerUserInteraction().
     */
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
        // 1. Verificar operación activa o cerrada
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

        // 2. Verificar recencia de interacciones
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

        UserInteraction previous = findMostRecentInteractionBefore(
                current.getClient(), current.getTimestamp());
        if (previous == null) return;

        Neighborhood from = neighborhoodOf(previous);
        if (from == null) return;

        // Nivel CITY — solo si las ciudades difieren
        if (from.getCity() != null && to.getCity() != null
                && !from.getCity().equals(to.getCity())) {
            registerZoneEdge(
                    cityKey(from), buildCityNode(from),
                    cityKey(to),   buildCityNode(to)
            );
        }

        // Nivel ZONE — si la combinación ciudad+zona difiere
        if (from.getZone() != null && to.getZone() != null
                && !zoneKey(from).equals(zoneKey(to))) {
            registerZoneEdge(
                    zoneKey(from), buildZoneNode(from),
                    zoneKey(to),   buildZoneNode(to)
            );
        }

        // Nivel NEIGHBORHOOD — solo si ciudad y zona coinciden pero el barrio difiere
        if (sameZone(from, to)
                && from.getName() != null && to.getName() != null
                && !from.getName().equalsIgnoreCase(to.getName())) {
            registerZoneEdge(
                    neighborhoodKey(from), buildNeighborhoodNode(from),
                    neighborhoodKey(to),   buildNeighborhoodNode(to)
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

    // ── helpers privados ──────────────────────────────────────────────────────

    private Neighborhood neighborhoodOf(UserInteraction i) {
        if (i == null || i.getProperty() == null) return null;
        return i.getProperty().getNeighborhood();
    }

    private UserInteraction findMostRecentInteractionBefore(Client client,
                                                            LocalDateTime reference) {
        UserInteraction best = null;
        for (InteractionType type : InteractionType.values()) {
            ArrayList<UserInteraction> list = client.getInteractionsByType(type);
            if (list == null) continue;
            for (int i = 0; i < list.size(); i++) {
                UserInteraction ui = list.get(i);
                if (ui.getTimestamp() == null) continue;
                if (!ui.getTimestamp().isBefore(reference)) continue;
                if (best == null || ui.getTimestamp().isAfter(best.getTimestamp())) {
                    best = ui;
                }
            }
        }
        return best;
    }

    private boolean sameZone(Neighborhood a, Neighborhood b) {
        return a.getCity() != null && a.getCity().equals(b.getCity())
                && a.getZone() != null && a.getZone().equals(b.getZone());
    }

    // ── claves de nodo ────────────────────────────────────────────────────────

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

    // ── construcción de nodos ─────────────────────────────────────────────────

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