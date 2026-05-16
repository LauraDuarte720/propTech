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

    private City city;

    private Zone zone;

    private String nameNeighborhood;

    @AssertTrue(message = "If zone is provided, city must also be provided")
    public boolean isZoneValid() {
        if (zone == null) return true;
        return city != null;
    }

    @AssertTrue(message = "If neighborhood is provided, both city and zone must be provided")
    public boolean isNeighborhoodValid() {
        if (nameNeighborhood == null || nameNeighborhood.isBlank()) return true;
        return city != null && zone != null;
    }
}