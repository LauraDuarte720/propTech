package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.AdminActionType;
import co.edu.uniquindio.com.proptech.domain.enums.AdminEntityType;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminActionLog {
    private String id;
    private AdminEntityType entity;
    private AdminActionType action;
    private String description;
    private LocalDateTime timestamp;
    private String performedBy;
    private String entityId;
    
    @Builder.Default
    private boolean undoable = true;
}