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


    public AdminActionService(AdminActionLogRepository repository,
                              @Lazy PropertyService propertyService,
                              @Lazy AgentService agentService) {
        this.repository = repository;
        this.propertyService = propertyService;
        this.agentService = agentService;
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


    public Object getEntityById(AdminEntityType entityType, String entityId) {
        return switch (entityType) {
            case PROPERTY -> propertyService.getPropertyByCode(entityId);
            case AGENT -> agentService.getAgentByCedula(entityId);
        };
    }

    public AdminActionLog peekLastAction() {
        if (repository.isEmpty()) {
            throw new NoAdminActionsToUndo("No admin actions to undo");
        }
        return repository.peekUndo();
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
                    case UPDATE -> propertyService.undoLastChange(log.getEntityId());
                }
            }
        }
    }

    public Queue<AdminActionLog> getHistory() {
        return repository.getHistory();
    }
}