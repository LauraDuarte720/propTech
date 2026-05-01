package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BasicAlertDto {

    private String id;

    private LocalDateTime timestamp;

    private boolean reviewed;

    private AgentDtoReturn agent;

    private ClientDtoReturn client;

    private OperationDto operation;

    private PropertyDto property;

    private VisitDto visit;

    private AlertType alertType;

}