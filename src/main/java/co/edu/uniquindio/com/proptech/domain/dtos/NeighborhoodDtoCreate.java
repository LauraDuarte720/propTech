package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NeighborhoodDtoCreate {

    @NotBlank(message = "Neighborhood name is required")
    @Pattern(
            regexp = "^[\\p{L}0-9\\- ]+$",
            message = "Neighborhood name can only contain letters, numbers, spaces, and hyphens"
    )
    @Size(min = 2, max = 100, message = "Neighborhood name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Zone is required")
    private Zone zone;

    @NotNull(message = "City is required")
    private City city;
}