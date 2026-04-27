package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.math.BigDecimal;

@Entity
@Table(name = "operation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Property property;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Client client;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Agent agent;
    @Column (nullable = false)
    private LocalDate dateInitial;
    @Column (nullable = false)
    private LocalDate dateFinal;
    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private OperationType operationType;
    @Column (nullable = false)
    private double value;
    @Column (nullable = false)
    private double commission;
    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private ProcessStatus processStatus;
}