package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInteractionDtoCreate {

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Property ID is required")
    private String propertyId;

    @NotNull(message = "Interaction type is required")
    private InteractionType interactionType;
}