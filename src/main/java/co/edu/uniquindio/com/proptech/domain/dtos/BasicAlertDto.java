package co.edu.uniquindio.com.proptech.domain.dtos;
import co.edu.uniquindio.com.proptech.domain.enums.AlertType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Timestamp is required")
    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;

    private boolean reviewed;

    @Valid
    private AgentDtoReturn agent;

    @Valid
    private ClientDtoReturn client;

    @Valid
    private OperationDtoCreate operation;

    @Valid
    private PropertyDtoReturn property;

    @Valid
    private VisitDtoReturn visit;

    @NotNull(message = "Alert type is required")
    private AlertType alertType;
}