package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import lombok.*;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInteraction {

    private String id;

    private InteractionType interactionType;
    
    private LocalDateTime timestamp;

    private Client client;

    private Property property;
}