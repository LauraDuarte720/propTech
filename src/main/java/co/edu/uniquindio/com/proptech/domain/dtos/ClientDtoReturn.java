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
public class ClientDtoReturn {

    @NotBlank(message = "Cedula cannot be empty")
    private String cedula;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @PositiveOrZero(message = "Budget cannot be negative")
    private double budget;

    @Min(value = 0, message = "Minimum bedrooms cannot be negative")
    private int minBedrooms;

    @NotNull(message = "Client type cannot be null")
    private ClientType clientType;

    @NotNull(message = "Search status cannot be null")
    private SearchStatus searchStatus;

    @NotNull(message = "Desired property type cannot be null")
    private PropertyType desiredPropertyType;
}