package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.VisitType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDtoCreate {

    private String clientId;

    @NotBlank(message = "Property ID is required")
    private String propertyId;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotBlank(message = "Agent ID is required")
    private String agentId;

    @NotNull(message = "Visit type is required")
    private VisitType visitType;

    @Size(max = 500, message = "Post visit notes must not exceed 500 characters")
    private String postVisitNotes;
}