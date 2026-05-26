package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.SupportRequestStatus;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportRequestDtoReturn {
    private String id;
    private ClientDtoReturn client;
    private PropertyDtoReturn property;
    private AgentDtoReturn agent;

    @NotBlank(message = "Message is required")
    @Size(min = 3, max = 500, message = "Message must be between 10 and 500 characters")
    private String message;

    private LocalDateTime date;                // ← agregado
    private SupportRequestStatus status;
}