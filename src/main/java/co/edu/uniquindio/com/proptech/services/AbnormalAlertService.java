package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.AbnormalAlertRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import jakarta.websocket.RemoteEndpoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static co.edu.uniquindio.com.proptech.domain.enums.AlertAbnormalType.HIGH_VISITS_NO_CLOSING;

@Service
public class AbnormalAlertService {

    // ── Umbrales configurables ──────────────────────────────────────────────
    private static final int MAX_COMPLETED_VISITS_WITHOUT_CLOSING = 5;
    private static final int MAX_CLIENT_VISITS_IN_WINDOW           = 3;
    private static final int CLIENT_VISIT_WINDOW_DAYS              = 30;
    private static final int MAX_AGENT_ACTIVE_VISITS               = 10;
    private static final int MAX_PRICE_CHANGES_IN_WINDOW           = 3;
    private static final int PRICE_CHANGE_WINDOW_DAYS              = 30;
    private static final int MAX_ZONE_VISITS_IN_WINDOW             = 5;
    private static final int ZONE_VISIT_WINDOW_DAYS                = 7;

    private final VisitService visitService;
    private final OperationService operationService;
    private final PropertyService propertyService;
    private final AbnormalAlertRepository abnormalAlertRepository;

    public AbnormalAlertService(VisitService visitService,
                                            OperationService operationService,
                                            PropertyService propertyService,
                                            AbnormalAlertRepository abnormalAlertRepository) {
        this.visitService     = visitService;
        this.operationService = operationService;
        this.propertyService  = propertyService;
        this.abnormalAlertRepository = abnormalAlertRepository;
    }

    public void runAllDetectors() {
        detectHighVisitsNoClosing();
        detectClientMultipleVisitsNoContinuity();
        detectAgentExcessiveOverload();
        detectPropertyPriceChanges();
        detectZoneInterestConcentration();
    }


    private void detectHighVisitsNoClosing() {
        LinkedList<Visit> allVisits = safeGetAllVisits();

        // Acumular propiedades ya evaluadas para no generar alerta doble
        LinkedList<String> evaluated = new LinkedList<>();

        for (Visit visit : allVisits) {
            Property property = visit.getProperty();
            if (property == null) continue;

            String code = property.getCode();
            if (containsCode(evaluated, code)) continue;
            evaluated.addLast(code);

            // Contar visitas COMPLETED para esta propiedad
            LinkedList<Visit> propertyVisits = safeGetVisitsByProperty(code);
            int completedCount = 0;
            for (Visit v : propertyVisits) {
                if (v.getStatus() == VisitStatus.COMPLETED) completedCount++;
            }

            if (completedCount < MAX_COMPLETED_VISITS_WITHOUT_CLOSING) continue;

            // Verificar que no exista ningún cierre (operación de venta o arriendo)
            boolean hasClosure = hasClosingOperation(code);
            if (hasClosure) continue;

            AttentionLevel level = completedCount >= MAX_COMPLETED_VISITS_WITHOUT_CLOSING * 2
                    ? AttentionLevel.HIGH
                    : AttentionLevel.MEDIUM;

            AbnormalAlert alert = AbnormalAlert.builder()
                    .id(CodeGenerator.generateAbnormalAlertCode(HIGH_VISITS_NO_CLOSING))
                    .timestamp(LocalDateTime.now())
                    .reviewed(false)
                    .property(property)
                    .alertAbnormalType(HIGH_VISITS_NO_CLOSING)
                    .attentionLevel(level)
                    .build();

            abnormalAlertRepository.save(alert);
        }
    }



    private void detectClientMultipleVisitsNoContinuity() {
        LinkedList<Visit> allVisits = safeGetAllVisits();
        LinkedList<String> evaluated = new LinkedList<>();

        for (Visit visit : allVisits) {
            Client client = visit.getClient();
            if (client == null) continue;

            String cedula = client.getCedula();
            if (containsCode(evaluated, cedula)) continue;
            evaluated.addLast(cedula);

            LinkedList<Visit> clientVisits = safeGetVisitsByClient(cedula);
            LocalDateTime windowStart = LocalDateTime.now().minusDays(CLIENT_VISIT_WINDOW_DAYS);

            int visitsInWindow   = 0;
            boolean hasCompleted = false;

            for (Visit v : clientVisits) {
                if (v.getDate() == null) continue;
                if (v.getDate().isAfter(windowStart)) {
                    visitsInWindow++;
                    if (v.getStatus() == VisitStatus.COMPLETED) hasCompleted = true;
                }
            }

            if (visitsInWindow < MAX_CLIENT_VISITS_IN_WINDOW) continue;
            if (hasCompleted) continue; // hay continuidad, no es anormal

            AttentionLevel level = visitsInWindow >= MAX_CLIENT_VISITS_IN_WINDOW * 2
                    ? AttentionLevel.HIGH
                    : AttentionLevel.MEDIUM;

            AbnormalAlert alert = AbnormalAlert.builder()
                    .id(CodeGenerator.generateAbnormalAlertCode(AlertAbnormalType.CLIENT_MULTIPLE_VISITS_NO_CONTINUITY))
                    .timestamp(LocalDateTime.now())
                    .reviewed(false)
                    .client(client)
                    .alertAbnormalType(AlertAbnormalType.CLIENT_MULTIPLE_VISITS_NO_CONTINUITY)
                    .attentionLevel(level)
                    .build();

            abnormalAlertRepository.save(alert);
        }
    }



    private void detectAgentExcessiveOverload() {
        LinkedList<Visit> allVisits = safeGetAllVisits();
        LinkedList<String> evaluated = new LinkedList<>();

        for (Visit visit : allVisits) {
            Agent agent = visit.getAgent();
            if (agent == null) continue;

            String cedula = agent.getCedula();
            if (containsCode(evaluated, cedula)) continue;
            evaluated.addLast(cedula);

            LinkedList<Visit> agentVisits = safeGetVisitsByAgent(cedula);
            int activeCount = 0;

            for (Visit v : agentVisits) {
                if (v.getStatus() == VisitStatus.PENDING
                        || v.getStatus() == VisitStatus.CONFIRMED) {
                    activeCount++;
                }
            }

            if (activeCount < MAX_AGENT_ACTIVE_VISITS) continue;

            AttentionLevel level = activeCount >= MAX_AGENT_ACTIVE_VISITS * 2
                    ? AttentionLevel.HIGH
                    : AttentionLevel.MEDIUM;

            AbnormalAlert alert = AbnormalAlert.builder()
                    .id(CodeGenerator.generateAbnormalAlertCode(AlertAbnormalType.AGENT_EXCESSIVE_OVERLOAD))
                    .timestamp(LocalDateTime.now())
                    .reviewed(false)
                    .agent(agent)
                    .alertAbnormalType(AlertAbnormalType.AGENT_EXCESSIVE_OVERLOAD)
                    .attentionLevel(level)
                    .build();

            abnormalAlertRepository.save(alert);
        }
    }


    private void detectPropertyPriceChanges() {
        LinkedList<Property> allProperties = propertyService.getAllProperties().values();

        for (Property property : allProperties) {
            LinkedList<PriceHistory> history = property.getPriceHistory();
            if (history == null || history.isEmpty()) continue;

            LocalDateTime windowStart = LocalDateTime.now().minusDays(PRICE_CHANGE_WINDOW_DAYS);
            int changesInWindow = 0;

            for (PriceHistory record : history) {
                if (record.getChangedAt().isAfter(windowStart)) changesInWindow++;
            }

            if (changesInWindow < MAX_PRICE_CHANGES_IN_WINDOW) continue;

            AttentionLevel level = changesInWindow >= MAX_PRICE_CHANGES_IN_WINDOW * 2
                    ? AttentionLevel.HIGH
                    : AttentionLevel.MEDIUM;

            AbnormalAlert alert = AbnormalAlert.builder()
                    .id(CodeGenerator.generateAbnormalAlertCode(AlertAbnormalType.PROPERTY_PRICE_CHANGE))
                    .timestamp(LocalDateTime.now())
                    .reviewed(false)
                    .property(property)
                    .alertAbnormalType(AlertAbnormalType.PROPERTY_PRICE_CHANGE)
                    .attentionLevel(level)
                    .build();

            abnormalAlertRepository.save(alert);
        }
    }



    private void detectZoneInterestConcentration() {
        LinkedList<Visit> allVisits = safeGetAllVisits();
        LocalDateTime windowStart = LocalDateTime.now().minusDays(ZONE_VISIT_WINDOW_DAYS);

        HashTable<String, int[]> zoneCount = new HashTable<>();
        HashTable<String, Property> zoneProperty = new HashTable<>();

        for (Visit visit : allVisits) {
            if (visit.getDate() == null || visit.getDate().isBefore(windowStart)) continue;
            Property property = visit.getProperty();
            if (property == null || property.getNeighborhood() == null
                    || property.getNeighborhood().getZone() == null) continue;

            String zoneKey = property.getNeighborhood().getZone().name();

            int[] count = zoneCount.get(zoneKey);
            if (count == null) {
                zoneCount.put(zoneKey, new int[]{1});
                zoneProperty.put(zoneKey, property);
            } else {
                count[0]++;
            }
        }

        LinkedList<String> keys = zoneCount.keys();
        for (String zoneName : keys) {
            int[] count = zoneCount.get(zoneName);
            if (count[0] < MAX_ZONE_VISITS_IN_WINDOW) continue;

            Property representative = zoneProperty.get(zoneName);
            AttentionLevel level = count[0] >= MAX_ZONE_VISITS_IN_WINDOW * 2
                    ? AttentionLevel.HIGH
                    : AttentionLevel.MEDIUM;

            AbnormalAlert alert = AbnormalAlert.builder()
                    .id(CodeGenerator.generateAbnormalAlertCode(AlertAbnormalType.ZONE_INTEREST_CONCENTRATION))
                    .timestamp(LocalDateTime.now())
                    .reviewed(false)
                    .property(representative)
                    .alertAbnormalType(AlertAbnormalType.ZONE_INTEREST_CONCENTRATION)
                    .attentionLevel(level)
                    .build();

            abnormalAlertRepository.save(alert);
        }
    }





    private boolean hasClosingOperation(String propertyCode) {
        try {
            LinkedList<Operation> ops = operationService.getOperationsByProperty(propertyCode);
            for (Operation op : ops) {
                OperationType t = op.getOperationType();
                if (t == OperationType.SALE || t == OperationType.RENT) return true;
            }
        } catch (RuntimeException ignored) {}
        return false;
    }

    private boolean containsCode(LinkedList<String> list, String code) {
        for (String s : list) {
            if (s.equals(code)) return true;
        }
        return false;
    }

    private LinkedList<Visit> safeGetAllVisits() {
        return visitService.getAllVisits();
    }

    private LinkedList<Visit> safeGetVisitsByProperty(String code) {
        return visitService.getVisitsByProperty(code);
    }

    private LinkedList<Visit> safeGetVisitsByClient(String cedula) {
        return visitService.getVisitsByClient(cedula);
    }

    private LinkedList<Visit> safeGetVisitsByAgent(String cedula) {
        return visitService.getVisitsByAgent(cedula);
    }
}
