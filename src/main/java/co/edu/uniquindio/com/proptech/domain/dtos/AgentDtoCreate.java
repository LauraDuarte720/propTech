package co.edu.uniquindio.com.proptech.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDtoCreate {

    @NotBlank(message = "ID (cedula) is required")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "ID must contain only numeric characters"
    )
    @Size(min = 6, max = 20, message = "ID must be between 6 and 20 digits")
    private String cedula;

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Name must contain only letters and spaces"
    )
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username can only contain letters, numbers, dots, underscores, and hyphens"
    )
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must include uppercase, lowercase, number, and special character"
    )
    private String password;

    @NotBlank(message = "Contact information is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]+$",
            message = "Contact must be a valid phone number format"
    )
    @Size(min = 7, max = 20, message = "Contact must be between 7 and 20 characters")
    private String contact;

    @NotNull(message = "Assigned zone is required")
    @Valid
    private GeographicZoneDtoCreate assignedZone;

    @Min(value = 0, message = "Closed deals cannot be negative")
    @Max(value = 100000, message = "Closed deals exceeds realistic limit")
    private int closedDeals;
}