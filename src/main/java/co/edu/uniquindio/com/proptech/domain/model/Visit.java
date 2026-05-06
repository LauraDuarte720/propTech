package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    private String id;
    
    private Client client;
    
    private Property property;
    
    private LocalDateTime date;

    private Agent agent;

    private VisitStatus status;
     
    private String postVisitNotes;

    private LocalDateTime createdAt;
}