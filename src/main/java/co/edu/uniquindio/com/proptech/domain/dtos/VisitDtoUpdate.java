package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.VisitType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDtoUpdate {

    @NotBlank(message = "Visit ID is required")
    private String id;

    private String clientId;

    private String propertyId;

    private LocalDateTime date;

    private String agentId;

    private VisitStatus status;

    private VisitType visitType;

    @Size(max = 500, message = "Post visit notes must not exceed 500 characters")
    private String postVisitNotes;
}