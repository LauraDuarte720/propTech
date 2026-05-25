package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.AdminActionType;
import co.edu.uniquindio.com.proptech.domain.enums.AdminEntityType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminActionLogDtoReturn {

    private String id;

    private AdminEntityType entity;

    private AdminActionType action;

    private String description;

    private LocalDateTime timestamp;

    private String performedBy;

    private String entityId;
}