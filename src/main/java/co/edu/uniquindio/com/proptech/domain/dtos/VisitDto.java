package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDto {

    private String id;

    private ClientDtoReturn client;

    private PropertyDto property;

    private LocalDateTime date;

    private AgentDtoReturn agent;

    private VisitStatus status;

    private String postVisitNotes;
}