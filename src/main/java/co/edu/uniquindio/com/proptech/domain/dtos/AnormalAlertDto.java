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

    private LocalDateTime timestamp;

    private boolean reviewed;

    private AgentDtoReturn agent;

    private ClientDtoReturn client;

    private OperationDtoCreate operation;

    private PropertyDtoReturn property;

    private VisitDtoReturn visit;

    private AlertAnormalType alertAnormalType;

    private AttentionLevel attentionLevel;
}