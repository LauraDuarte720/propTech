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
public class GeographicZoneDtoCreate {

    @NotNull(message = "Zone is required")
    private Zone zone;

    @NotNull(message = "City is required")
    private City city;

    @NotBlank(message = "Neighborhood ID is required")
    @Size(min = 2, max = 100, message = "Neighborhood ID must be between 2 and 100 characters")
    private String neighborhoodId;
}