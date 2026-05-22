package co.edu.uniquindio.com.proptech.domain.model;
import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class ZoneNode {

    // Nivel de granularidad que representa este nodo
    public enum Level { CITY, ZONE, NEIGHBORHOOD }

    private Level level;
    private City  city;
    private Zone  zone;           // null si level == CITY
    private String neighborhoodName; // null si level != NEIGHBORHOOD

    public String getLabel() {
        return switch (level) {
            case CITY         -> city.toString();
            case ZONE         -> zone.toString() + " - " + city.toString();
            case NEIGHBORHOOD -> neighborhoodName + " (" + zone + ", " + city + ")";
        };
    }
}