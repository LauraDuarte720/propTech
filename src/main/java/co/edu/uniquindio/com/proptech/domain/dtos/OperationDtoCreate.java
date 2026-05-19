package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDtoCreate {

    @NotBlank(message = "Property ID is required")
    @Size(max = 50, message = "Property ID must not exceed 50 characters")
    private String propertyId;

    @NotBlank(message = "Client ID is required")
    @Size(max = 50, message = "Client ID must not exceed 50 characters")
    private String clientId;

    @NotBlank(message = "Agent ID is required")
    @Size(max = 50, message = "Agent ID must not exceed 50 characters")
    private String agentId;

    @NotBlank(message = "Initial date is mandatory")
    private LocalDate dateInitial;

    private LocalDate dateFinal;

    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    @Positive(message = "Value must be greater than 0")
    @Max(value = 10_000_000_000L, message = "Value exceeds realistic limit")
    private double value;

    @PositiveOrZero(message = "Commission cannot be negative")
    @Max(value = 1_000_000_000L, message = "Commission exceeds realistic limit")
    private double commission;

    @AssertTrue(message = "Initial and final dates are required when operation type is CONTRACT_RENEWAL")
    public boolean isDatesRequiredForContract() {
        if (operationType == null) return true;

        if (operationType == OperationType.CONTRACT_RENEWAL) {
            return dateFinal != null;
        }

        return true;
    }

    @AssertTrue(message = "Final date must be after or equal to initial date")
    public boolean isValidDateRange() {
        if (dateInitial == null || dateFinal == null) return true;
        return !dateFinal.isBefore(dateInitial);
    }
}