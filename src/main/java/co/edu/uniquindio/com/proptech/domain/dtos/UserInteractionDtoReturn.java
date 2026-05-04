package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInteractionDtoReturn {

    private String id;

    private InteractionType interactionType;

    private LocalDateTime timestamp;

    private ClientDtoReturn client;

    private PropertyDtoReturn property;
}