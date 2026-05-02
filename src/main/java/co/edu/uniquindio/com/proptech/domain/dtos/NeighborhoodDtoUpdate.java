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
public class NeighborhoodDtoUpdate {

    @NotBlank(message = "Neighborhood name is required")
    @Pattern(
            regexp = "^[\\p{L}0-9\\- ]+$",
            message = "Neighborhood name can only contain letters, numbers, spaces, and hyphens"
    )
    @Size(min = 2, max = 100, message = "Neighborhood name must be between 2 and 100 characters")
    private String name;

    private Zone zone;

    private City city;
}