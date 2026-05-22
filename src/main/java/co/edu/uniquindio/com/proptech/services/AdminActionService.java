package co.edu.uniquindio.com.proptech.services;
import co.edu.uniquindio.com.proptech.domain.enums.AdminActionType;
import co.edu.uniquindio.com.proptech.domain.enums.AdminEntityType;
import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.repositories.AdminActionLogRepository;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminActionService {

    private final AdminActionLogRepository repository;
    private final PropertyService propertyService;
    private final AgentService agentService;
    private final PropertyAssignmentService propertyAssignmentService;

    public AdminActionService(AdminActionLogRepository repository, PropertyService propertyService,
                              AgentService agentService, PropertyAssignmentService propertyAssignmentService) {
        this.repository = repository;
        this.propertyService = propertyService;
        this.agentService = agentService;
        this.propertyAssignmentService = propertyAssignmentService;
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

    public Object getEntityById(AdminEntityType entityType, String entityId) {
        return switch (entityType) {
            case PROPERTY -> propertyService.getPropertyByCode(entityId);
            case AGENT -> agentService.getAgentByCedula(entityId);
        };
    }

    public void undoLastAction() {
        if (repository.isEmpty()) {
            throw new RuntimeException("No admin actions to undo");
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
                }
            }
        }
    }

    public Queue<AdminActionLog> getHistory(AdminEntityType entityType) {
        return repository.getHistory();
    }
}