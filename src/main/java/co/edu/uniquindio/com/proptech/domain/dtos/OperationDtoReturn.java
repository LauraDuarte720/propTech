package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.enums.ProcessStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDtoReturn {

    @NotBlank(message = "Operation ID is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-]+$",
            message = "Operation ID can only contain letters, numbers, and hyphens"
    )
    @Size(max = 50, message = "Operation ID must not exceed 50 characters")
    private String id;

    @NotNull(message = "Property is required")
    @Valid
    private PropertyDtoReturn property;

    @NotNull(message = "Client is required")
    @Valid
    private ClientDtoReturn client;

    @NotNull(message = "Agent is required")
    @Valid
    private AgentDtoReturn agent;


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

    @NotNull(message = "Process status is required")
    private ProcessStatus processStatus;


    @AssertTrue(message = "Initial and final dates are required when operation type is CONTRACT")
    public boolean isDatesRequiredForContract() {
        if (operationType == null) return true;

        if (operationType == OperationType.CONTRACT_RENEWAL) {
            return dateInitial != null && dateFinal != null;
        }

        return true;
    }

    @AssertTrue(message = "Final date must be after or equal to initial date")
    public boolean isValidDateRange() {
        if (dateInitial == null || dateFinal == null) return true;
        return !dateFinal.isBefore(dateInitial);
    }
}