package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {

    private int id;
    
    private Property property;
    
    private Client client;
    
    private Agent agent;
     
    private LocalDate dateInitial;
     
    private LocalDate dateFinal;

    private OperationType operationType;
     
    private double value;
     
    private double commission;

    private ProcessStatus processStatus;
}