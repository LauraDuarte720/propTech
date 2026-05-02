package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDtoUpdate {

    @NotBlank(message = "Operation ID is required")
    @Size(max = 50, message = "Operation ID must not exceed 50 characters")
    private String id;

    @Size(max = 50, message = "Property ID must not exceed 50 characters")
    private String propertyId;

    @Size(max = 50, message = "Client ID must not exceed 50 characters")
    private String clientId;

    @Size(max = 50, message = "Agent ID must not exceed 50 characters")
    private String agentId;

    private LocalDate dateInitial;

    private LocalDate dateFinal;

    private OperationType operationType;

    @Positive(message = "Value must be greater than 0")
    @Max(value = 10_000_000_000L, message = "Value exceeds realistic limit")
    private Double value;

    @PositiveOrZero(message = "Commission cannot be negative")
    @Max(value = 1_000_000_000L, message = "Commission exceeds realistic limit")
    private Double commission;

    private ProcessStatus processStatus;

    @AssertTrue(message = "Final date must be after or equal to initial date")
    public boolean isValidDateRange() {
        if (dateInitial == null || dateFinal == null) return true;
        return !dateFinal.isBefore(dateInitial);
    }
}