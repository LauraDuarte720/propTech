package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name = "operation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    private int id;
    private Property property;
    private Client client;
    private Agent agent;
    private LocalDate dateInitial;
    private LocalDate dateFinal;
    private OperationType operationType;
    private BigDecimal value;
    private BigDecimal commission;
    private ProcessStatus processStatus;
}