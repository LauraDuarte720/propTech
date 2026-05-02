package co.edu.uniquindio.com.proptech.domain.dtos;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeographicZoneDtoReturn {

    @NotBlank(message = "ID is required")
    @Size(min = 1, max = 50, message = "ID must be between 1 and 50 characters")
    private String id;

    @NotNull(message = "Zone is required")
    private Zone zone;

    @NotNull(message = "City is required")
    private City city;

    @NotNull(message = "Neighborhood is required")
    @Valid
    private NeighborhoodDtoReturn neighborhood;
}