package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;

import java.util.Optional;

public interface GeographicZoneRepository {
    GeographicZone save(GeographicZone geographicZone);
    Optional<GeographicZone> findById(String id);
    boolean deleteById(String id);
    GeographicZone update(GeographicZone geographicZone);
    ArrayList<GeographicZone> getGeographicZones();
    Optional<GeographicZone> findByCityZoneNeighborhood(City city, Zone zone, String nameNeighborhood);
}
