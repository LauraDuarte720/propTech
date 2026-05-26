package co.edu.uniquindio.com.proptech.mappers.impl;


import co.edu.uniquindio.com.proptech.domain.dtos.AdminActionLogDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.AdminActionLog;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import org.springframework.stereotype.Component;

@Component
public class AdminActionLogMapper implements MapperOnlyDto<AdminActionLog, AdminActionLogDtoReturn> {

    @Override
    public AdminActionLogDtoReturn toDto(AdminActionLog entity) {
        return AdminActionLogDtoReturn.builder()
                .id(entity.getId())
                .entity(entity.getEntity())
                .action(entity.getAction())
                .description(entity.getDescription())
                .timestamp(entity.getTimestamp())
                .performedBy(entity.getPerformedBy())
                .entityId(entity.getEntityId())
                .undoable(entity.isUndoable())
                .build();
    }
}