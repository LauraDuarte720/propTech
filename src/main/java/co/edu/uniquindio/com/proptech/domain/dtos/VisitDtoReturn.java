package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.VisitType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDtoReturn {

    private String id;

    private ClientDtoReturn client;

    private PropertyDtoReturn property;

    private AgentDtoReturn agent;

    private LocalDateTime date;

    private VisitStatus status;

    private VisitType visitType;

    private String postVisitNotes;

    private LocalDateTime createdAt;
}