package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.BasicAlertRepository;
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
    private final OperationService operationService;
    private final PropertyService propertyService;
    private final VisitService visitService;
    private final ClientService clientService;

    // Cola normal: todas las alertas pendientes de revisión
    private final Queue<Alert> pendingAlerts = new Queue<>();

    // Cola de prioridad: alertas urgentes ordenadas por timestamp (más reciente primero)
    private final PriorityQueue<BasicAlert> priorityAlerts = new PriorityQueue<>(
            Comparator.comparing(BasicAlert::getTimestamp).reversed()
    );

    public BasicAlertService(BasicAlertRepository basicAlertRepository,
                        OperationService operationService,
                        PropertyService propertyService,
                        VisitService visitService,
                        ClientService clientService) {
        this.basicAlertRepository = basicAlertRepository;
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

    // ─── Helper para crear y enrutar la alerta ───────────────────────

    private BasicAlert buildAndRoute(AlertType type, Operation operation,
                                     Property property, Visit visit, Client client) {
        BasicAlert alert = BasicAlert.builder()
                .id(CodeGenerator.generateAlertCode(type))
                .alertType(type)
                .timestamp(LocalDateTime.now())
                .reviewed(false)
                .operation(operation)
                .property(property)
                .visit(visit)
                .client(client)
                .build();

        basicAlertRepository.save(alert);

        if (type == AlertType.CONTRACT_EXPIRING) {
            priorityAlerts.add(alert);
        } else {
            pendingAlerts.enqueue(alert);
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
                buildAndRoute(AlertType.CONTRACT_EXPIRING, op, null, null, null);
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
                buildAndRoute(AlertType.PROPERTY_NO_VISITS, null, property, null, null);
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
                buildAndRoute(AlertType.HIGH_DEMAND, null, property, null, null);
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
                    buildAndRoute(AlertType.PENDING_VISIT_CONFIRMATION,
                            null, null, visit, null);
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
                    buildAndRoute(AlertType.RESERVE_NO_CLOSURE, op, null, null, null);
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
                buildAndRoute(AlertType.INACTIVE_CLIENT, null, null, null, client);
            }
        }
    }

    // ─── Consultar colas ─────────────────────────────────────────────

    public BasicAlert getNextPriorityAlert() {
        if (priorityAlerts.isEmpty()) {
            throw new RuntimeException("No hay alertas urgentes pendientes");
        }
        BasicAlert alert = priorityAlerts.poll();
        alert.setReviewed(true);
        return alert;
    }

    public Alert getNextPendingAlert() {
        if (pendingAlerts.isEmpty()) {
            throw new RuntimeException("No hay alertas pendientes");
        }
        Alert alert = pendingAlerts.dequeue();
        alert.setReviewed(true);
        return alert;
    }
}