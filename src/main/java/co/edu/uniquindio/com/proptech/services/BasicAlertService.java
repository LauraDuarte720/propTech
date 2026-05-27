package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.BasicAlertRepository;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Service
public class BasicAlertService {

    private final BasicAlertRepository basicAlertRepository;
    private final AgentRepository agentRepository;
    private final OperationService operationService;
    private final PropertyService propertyService;
    private final VisitService visitService;
    private final ClientService clientService;

    public BasicAlertService(BasicAlertRepository basicAlertRepository,
                        AgentRepository agentRepository,
                        OperationService operationService,
                        PropertyService propertyService,
                        VisitService visitService,
                        ClientService clientService) {
        this.basicAlertRepository = basicAlertRepository;
        this.agentRepository = agentRepository;
        this.operationService = operationService;
        this.propertyService = propertyService;
        this.visitService = visitService;
        this.clientService = clientService;
    }

    // ─── Metodo principal ────────────────────────────────────────────

    public void generateAllAlerts() {
        createAlertContractExpiring();
        createAlertPropertyNoVisits();
        createAlertHighDemand();
        createAlertPendingVisitConfirmation();
        createAlertReserveNoClosure();
        createAlertInactiveClient();
    }

    public ArrayList<BasicAlert> getAllAlerts() {
        return basicAlertRepository.getAll();
    }

    public boolean hasPendingAlerts(String agentCedula) {
        Agent agent = agentRepository.findByCedula(agentCedula)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con cedula: " + agentCedula));
        for (BasicAlert a : agent.getBasicAlertQueue()) {
            if (!a.isReviewed()) return true;
        }
        return false;
    }

    public boolean hasPriorityAlerts(String agentCedula) {
        Agent agent = agentRepository.findByCedula(agentCedula)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con cedula: " + agentCedula));
        for (BasicAlert a : agent.getPriorityAlertQueue()) {
            if (!a.isReviewed()) return true;
        }
        return false;
    }

    // ─── Helper para crear y enrutar la alerta ───────────────────────

    private BasicAlert buildAndRoute(AlertType type, Operation operation,
                                     Property property, Visit visit, Client client, Agent agent) {
        BasicAlert alert = BasicAlert.builder()
                .id(CodeGenerator.generateAlertCode(type))
                .alertType(type)
                .timestamp(LocalDateTime.now())
                .reviewed(false)
                .operation(operation)
                .property(property)
                .visit(visit)
                .client(client)
                .agent(agent)
                .build();

        basicAlertRepository.save(alert);

        // Enrutar al agente correspondiente
        if (agent != null) {
            if (type == AlertType.CONTRACT_EXPIRING) {
                agent.addPriorityAlert(alert);
            } else {
                agent.enqueueBasicAlert(alert);
            }
        } else if (type == AlertType.INACTIVE_CLIENT) {
            // Enrutar a todos los asesores
            for (Agent a : agentRepository.getAgents().values()) {
                a.enqueueBasicAlert(alert);
            }
        }

        return alert;
    }

    // ─── 1. Contratos próximos a vencer (30 días) ────────────────────

    public void createAlertContractExpiring() {
        LinkedList<Operation> operations = operationService
                .getOperationsByType(OperationType.CONTRACT_RENEWAL);

        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);

            // Si ya está cancelada, no hay nada que alertar
            if (op.getProcessStatus() == ProcessStatus.CANCELLED) {
                continue;
            }

            long days = ChronoUnit.DAYS.between(LocalDate.now(), op.getDateFinal());
            if (days >= 0 && days <= 30) {
                if (op.getAgent() != null) {
                    if (!alertaYaExiste(AlertType.CONTRACT_EXPIRING, op.getId())) {
                        buildAndRoute(AlertType.CONTRACT_EXPIRING, op, op.getProperty(), null, null, op.getAgent());
                    }
                }
            }
        }
    }

    // ─── 2. Inmuebles sin visitas en 60 días ─────────────────────────

    public void createAlertPropertyNoVisits() {
        LinkedList<Visit> allVisits = visitService.getAllVisits();

        for (Property property : propertyService.getAllProperties().values()) {
            boolean hasRecentVisit = false;

            for (int i = 0; i < allVisits.size(); i++) {
                Visit visit = allVisits.get(i);
                if (visit.getProperty().getCode().equals(property.getCode())) {
                    long days = ChronoUnit.DAYS.between(
                            visit.getDate().toLocalDate(), LocalDate.now());
                    if (days <= 60) {
                        hasRecentVisit = true;
                        break;
                    }
                }
            }

            if (!hasRecentVisit) {
                if (property.getAgent() != null) {
                    if (!alertaYaExiste(AlertType.PROPERTY_NO_VISITS, property.getCode())) {
                        buildAndRoute(AlertType.PROPERTY_NO_VISITS, null, property, null, null, property.getAgent());
                    }
                }
            }
        }
    }

    // ─── 3. Propiedades con alta demanda (más de 5 visitas en 30 días)

    public void createAlertHighDemand() {
        LinkedList<Visit> allVisits = visitService.getAllVisits();

        for (Property property : propertyService.getAllProperties().values()) {
            int count = 0;

            for (int i = 0; i < allVisits.size(); i++) {
                Visit visit = allVisits.get(i);
                if (visit.getProperty().getCode().equals(property.getCode())) {
                    long days = ChronoUnit.DAYS.between(
                            visit.getDate().toLocalDate(), LocalDate.now());
                    if (days <= 30) count++;
                }
            }

            if (count > 5) {
                if (property.getAgent() != null) {
                    if (!alertaYaExiste(AlertType.HIGH_DEMAND, property.getCode())) {
                        buildAndRoute(AlertType.HIGH_DEMAND, null, property, null, null, property.getAgent());
                    }
                }
            }
        }
    }

    // ─── 4. Visitas pendientes por confirmar hace más de 24 horas ────

    public void createAlertPendingVisitConfirmation() {
        LinkedList<Visit> allVisits = visitService.getAllVisits();

        for (int i = 0; i < allVisits.size(); i++) {
            Visit visit = allVisits.get(i);
            if (visit.getStatus() == VisitStatus.PENDING) {
                long hours = ChronoUnit.HOURS.between(
                        visit.getCreatedAt(), LocalDateTime.now());
                if (hours >= 24) {
                    if (visit.getAgent() != null) {
                        if (!alertaYaExiste(AlertType.PENDING_VISIT_CONFIRMATION, visit.getId())) {
                            buildAndRoute(AlertType.PENDING_VISIT_CONFIRMATION, null, null, visit, null, visit.getAgent());
                        }
                    }
                }
            }
        }
    }

    // ─── 5. Operaciones creadas hace más de 30 días sin cerrar ───────

    public void createAlertReserveNoClosure() {
        LinkedList<Operation> allOperations = operationService.getAllOperations();

        for (int i = 0; i < allOperations.size(); i++) {
            Operation op = allOperations.get(i);
            if (op.getProcessStatus() == ProcessStatus.CREATED) {
                long days = ChronoUnit.DAYS.between(
                        op.getDateInitial(), LocalDate.now());
                if (days > 30) {
                    if (op.getAgent() != null) {
                        if (!alertaYaExiste(AlertType.RESERVE_NO_CLOSURE, op.getId())) {
                            buildAndRoute(AlertType.RESERVE_NO_CLOSURE, op, null, null, null, op.getAgent());
                        }
                    }
                }
            }
        }
    }

//     ─── 6. Clientes sin interacción en 30 días ──────────────────────

    public void createAlertInactiveClient() {
        for (Client client : clientService.getClients().values()) {
            boolean hasRecentInteraction = false;

            for (InteractionType type : InteractionType.values()) {
                ArrayList<UserInteraction> interactions =
                        client.getInteractionsByType(type);

                if (interactions != null) {
                    for (int i = 0; i < interactions.size(); i++) {
                        long days = ChronoUnit.DAYS.between(
                                interactions.get(i).getTimestamp().toLocalDate(),
                                LocalDate.now());
                        if (days <= 30) {
                            hasRecentInteraction = true;
                            break;
                        }
                    }
                }
                if (hasRecentInteraction) break;
            }

            if (!hasRecentInteraction) {
                if (!alertaYaExiste(AlertType.INACTIVE_CLIENT, client.getCedula())) {
                    buildAndRoute(AlertType.INACTIVE_CLIENT, null, null, null, client, null);
                }
            }
        }
    }

    // ─── Consultar colas ─────────────────────────────────────────────

    public BasicAlert getNextPriorityAlert(String agentCedula) {
        Agent agent = agentRepository.findByCedula(agentCedula)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con cedula: " + agentCedula));
        while (agent.hasPriorityAlerts()) {
            BasicAlert alert = agent.pollPriorityAlert();
            if (!alert.isReviewed()) {
                alert.setReviewed(true);
                basicAlertRepository.update(alert);
                return alert;
            }
        }
        throw new RuntimeException("No hay alertas urgentes pendientes para este agente");
    }

    public BasicAlert getNextPendingAlert(String agentCedula) {
        Agent agent = agentRepository.findByCedula(agentCedula)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con cedula: " + agentCedula));
        while (agent.hasBasicAlerts()) {
            BasicAlert alert = agent.dequeueBasicAlert();
            if (!alert.isReviewed()) {
                alert.setReviewed(true);
                basicAlertRepository.update(alert);
                return alert;
            }
        }
        throw new RuntimeException("No hay alertas pendientes para este agente");
    }

    public BasicAlert peekPriorityAlert(String agentCedula) {
        Agent agent = agentRepository.findByCedula(agentCedula)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con cedula: " + agentCedula));
        while (agent.hasPriorityAlerts()) {
            BasicAlert alert = agent.peekPriorityAlert();
            if (!alert.isReviewed()) {
                return alert;
            }
            // Si ya está revisada por otro agente, la removemos de nuestra cola
            agent.pollPriorityAlert();
        }
        throw new RuntimeException("No hay alertas urgentes pendientes para este agente");
    }

    // ─── Helper para evitar duplicados ──────────────────────────────
    private boolean alertaYaExiste(AlertType type, String contextId) {
        ArrayList<BasicAlert> all = basicAlertRepository.getAll();
        for (int i = 0; i < all.size(); i++) {
            BasicAlert a = all.get(i);
            if (a.getAlertType() != type || a.isReviewed()) continue;

            // Comparar por el id del contexto según el tipo
            switch (type) {
                case CONTRACT_EXPIRING:
                case RESERVE_NO_CLOSURE:
                    if (a.getOperation() != null && a.getOperation().getId().equals(contextId)) return true;
                    break;
                case PROPERTY_NO_VISITS:
                case HIGH_DEMAND:
                    if (a.getProperty() != null && a.getProperty().getCode().equals(contextId)) return true;
                    break;
                case PENDING_VISIT_CONFIRMATION:
                    if (a.getVisit() != null && a.getVisit().getId().equals(contextId)) return true;
                    break;
                case INACTIVE_CLIENT:
                    if (a.getClient() != null && a.getClient().getCedula().equals(contextId)) return true;
                    break;
            }
        }
        return false;
    }
}