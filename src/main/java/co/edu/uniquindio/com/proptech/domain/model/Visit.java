package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.VisitType;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit implements Cloneable {

    private String id;
    
    private Client client;
    
    private Property property;
    
    private LocalDateTime date;

    private Agent agent;

    private VisitStatus status;
     
    private String postVisitNotes;

    private LocalDateTime createdAt;

    private VisitType visitType;

    @Override
    public Visit clone() {
        try {
            Visit clone = (Visit) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}