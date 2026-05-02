package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDtoCreate {
    private String clientId;

    private String propertyId;

    private LocalDateTime date;

    private AgentDtoReturn agent;

    private VisitStatus status;

    private String postVisitNotes;
}