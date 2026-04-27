package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.*;

@Entity
@Table(name = "visit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    @Id
    private int id;
    private Client client;
    private Property property;
    private LocalDate date;
    private LocalTime time;
    private Agent agent;
    private VisitStatus status;
    private String postVisitNotes;
}