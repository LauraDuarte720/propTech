package co.edu.uniquindio.com.proptech.domain.dtos;


import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoReturn;
import co.edu.uniquindio.com.proptech.domain.enums.AlertAnormalType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
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
public class AnormalAlertDto {

    private String id;

    @NotNull(message = "Timestamp is required")
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
    private AlertAnormalType alertAnormalType;

    @NotNull(message = "Attention level is required")
    private AttentionLevel attentionLevel;
}