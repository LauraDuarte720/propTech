package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDtoUpdate {

    @NotBlank(message = "ID (cedula) is required")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "ID must contain only numeric characters"
    )
    @Size(min = 6, max = 20, message = "ID must be between 6 and 20 digits")
    private String cedula;

    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Name must contain only letters and spaces"
    )
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username can only contain letters, numbers, dots, underscores, and hyphens"
    )
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Password must include uppercase, lowercase, and number"
    )
    private String password;

    @Email(message = "Email must be a valid format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(
            regexp = "^[0-9+\\-() ]+$",
            message = "Phone must be a valid format"
    )
    @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 characters")
    private String phone;

    @Positive(message = "Budget must be greater than 0")
    @Max(value = 1_000_000_000, message = "Budget exceeds realistic limit")
    private Double budget;

    @Min(value = 0, message = "Minimum bedrooms cannot be negative")
    @Max(value = 100, message = "Minimum bedrooms exceeds realistic limit")
    private Integer minBedrooms;

    private ClientType clientType;

    private SearchStatus searchStatus;

    private PropertyType desiredPropertyType;
}