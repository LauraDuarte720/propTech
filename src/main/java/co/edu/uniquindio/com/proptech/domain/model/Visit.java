package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    private int id;
    
    private Client client;
    
    private Property property;
    
    private LocalDate date;
    
    private LocalTime time;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Agent agent;

    private VisitStatus status;
     
    private String postVisitNotes;
}