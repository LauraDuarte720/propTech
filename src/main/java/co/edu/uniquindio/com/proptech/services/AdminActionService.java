package co.edu.uniquindio.com.proptech.services;
import co.edu.uniquindio.com.proptech.domain.enums.AdminActionType;
import co.edu.uniquindio.com.proptech.domain.enums.AdminEntityType;
import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NoAdminActionsToUndo;
import co.edu.uniquindio.com.proptech.repositories.AdminActionLogRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AdminActionService {

    private final AdminActionLogRepository repository;
    private final PropertyService propertyService;
    private final AgentService agentService;
    private final PropertyAssignmentService propertyAssignmentService;
    private final GeographicZoneService geographicZoneService;

    public AdminActionService(AdminActionLogRepository repository,
                              @Lazy PropertyService propertyService,
                              @Lazy AgentService agentService,
                              PropertyAssignmentService propertyAssignmentService, GeographicZoneService geographicZoneService) {
        this.repository = repository;
        this.propertyService = propertyService;
        this.agentService = agentService;
        this.propertyAssignmentService = propertyAssignmentService;
        this.geographicZoneService = geographicZoneService;
    }

    public void log(AdminActionType action, AdminEntityType entity, String description,
                    String performedBy, String entityId) {
        AdminActionLog log = AdminActionLog.builder()
                .id(UUID.randomUUID().toString())
                .action(action)
                .entity(entity)
                .description(description)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .entityId(entityId)
                .build();
        repository.save(log);
    }

    public void logAssign(String description, String performedBy, String propertyCode, String agentId) {
        AdminActionLog log = AdminActionLog.builder()
                .id(UUID.randomUUID().toString())
                .action(AdminActionType.ASSIGN)
                .entity(AdminEntityType.PROPERTY)
                .description(description)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .entityId(propertyCode)
                .secondaryEntityId(agentId)
                .build();
        repository.save(log);
    }

    public void logUnassign(String description, String performedBy, String propertyCode, String agentId) {
        AdminActionLog log = AdminActionLog.builder()
                .id(UUID.randomUUID().toString())
                .action(AdminActionType.UNASSIGN)
                .entity(AdminEntityType.PROPERTY)
                .description(description)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .entityId(propertyCode)
                .secondaryEntityId(agentId)
                .build();
        repository.save(log);
    }


    public void logUpdateZone(String agentCedula, String previousZoneId, ArrayList<String> affectedPropertyCodes) {
        AdminActionLog log = AdminActionLog.builder()
                .id(UUID.randomUUID().toString())
                .action(AdminActionType.UPDATE_ZONE)
                .entity(AdminEntityType.AGENT)
                .description("Agent zone updated: " + agentCedula)
                .performedBy("Admin")
                .timestamp(LocalDateTime.now())
                .entityId(agentCedula)
                .secondaryEntityId(previousZoneId)
                .affectedEntityIds(affectedPropertyCodes)
                .build();
        repository.save(log);
    }

    public Object getEntityById(AdminEntityType entityType, String entityId) {
        return switch (entityType) {
            case PROPERTY -> propertyService.getPropertyByCode(entityId);
            case AGENT -> agentService.getAgentByCedula(entityId);
        };
    }

    public void undoLastAction() {
        if (repository.isEmpty()) {
            throw new NoAdminActionsToUndo("No admin actions to undo");
        }
        AdminActionLog log = repository.pop();

        switch (log.getEntity()) {
            case PROPERTY -> {
                switch (log.getAction()) {
                    case CREATE -> propertyService.deleteProperty(log.getEntityId());
                    case PUBLISH -> propertyService.unpublishProperty(log.getEntityId());
                    case UNPUBLISH -> propertyService.publishProperty(log.getEntityId());
                    case ASSIGN -> propertyAssignmentService.removeAgentFromProperty(log.getEntityId(), log.getSecondaryEntityId());
                    case UNASSIGN -> propertyAssignmentService.assignAgent(log.getEntityId(), log.getSecondaryEntityId());
                }
            }
            case AGENT -> {
                switch (log.getAction()) {
                    case CREATE -> agentService.deleteAgent(log.getEntityId());
                    case UPDATE_ZONE -> {
                        Agent agent = agentService.getAgentByCedula(log.getEntityId());
                        GeographicZone previousZone = log.getSecondaryEntityId() != null
                                ? geographicZoneService.getGeographicZoneById(log.getSecondaryEntityId())
                                : null;
                        ArrayList<String> affected = log.getAffectedEntityIds() != null
                                ? log.getAffectedEntityIds()
                                : new ArrayList<>();
                        agentService.restoreGeographicZone(previousZone, agent, affected);
                    }
                }
            }
        }
    }

    public Queue<AdminActionLog> getHistory(AdminEntityType entityType) {
        return repository.getHistory();
    }
}