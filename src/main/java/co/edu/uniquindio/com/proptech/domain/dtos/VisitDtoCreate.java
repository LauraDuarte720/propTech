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
    private ClientDtoReturn client;

    private PropertyDtoReturn property;

    private LocalDateTime date;

    private AgentDtoReturn agent;

    private VisitStatus status;

    private String postVisitNotes;
}