package co.edu.uniquindio.com.proptech.utils;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import org.springframework.stereotype.Component;

@Component
public class ZoneMatcher {

    private ZoneMatcher() {}

    public boolean match(GeographicZone zone, Neighborhood neighborhood) {
        if (neighborhood == null) return false;
        if (zone == null) return true;
        if (!zone.getCity().equals(neighborhood.getCity())) return false;
        if (zone.getZone() != null && !zone.getZone().equals(neighborhood.getZone())) return false;
        if (zone.getNameNeighborhood() != null && !zone.getNameNeighborhood().equals(neighborhood.getName())) return false;
        return true;
    }
}