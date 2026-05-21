package co.edu.uniquindio.com.proptech.services;
import co.edu.uniquindio.com.proptech.domain.enums.AdminActionType;
import co.edu.uniquindio.com.proptech.domain.enums.AdminEntityType;
import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.repositories.AdminActionLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminActionService {

    private final AdminActionLogRepository repository;

    public AdminActionService(AdminActionLogRepository repository) {
        this.repository = repository;
    }

    public void log(AdminActionType action, AdminEntityType entity, String description, String performedBy) {

        AdminActionLog log = AdminActionLog.builder()
                .id(UUID.randomUUID().toString())
                .action(action)
                .entity(entity)
                .description(description)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(log);
    }
}
