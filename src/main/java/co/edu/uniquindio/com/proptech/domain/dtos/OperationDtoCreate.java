package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import lombok.*;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDtoCreate {

    private String id;

    private PropertyDtoReturn property;

    private ClientDtoReturn client;

    private AgentDtoReturn agent;

    private LocalDate dateInitial;

    private LocalDate dateFinal;

    private OperationType operationType;

    private double value;

    private double commission;

    private ProcessStatus processStatus;
}