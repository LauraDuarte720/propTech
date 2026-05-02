package co.edu.uniquindio.com.proptech.domain.dtos;
import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDtoCreate {

    @NotBlank(message = "Cedula is required")
    @Pattern(regexp = "^[0-9]+$", message = "Cedula must contain only numbers")
    private String cedula;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "Phone must contain 7 to 15 digits")
    private String phone;

    @Positive(message = "Budget must be greater than 0")
    private double budget;

    @Min(value = 0, message = "Minimum bedrooms cannot be negative")
    @Max(value = 20, message = "Too many bedrooms requested")
    private int minBedrooms;

    @NotNull(message = "Client type is required")
    private ClientType clientType;

    @NotNull(message = "Search status is required")
    private SearchStatus searchStatus;

    @NotNull(message = "Desired property type is required")
    private PropertyType desiredPropertyType;
}