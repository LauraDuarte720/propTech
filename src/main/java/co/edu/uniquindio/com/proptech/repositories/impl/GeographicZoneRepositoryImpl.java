package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.GeographicZoneRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class GeographicZoneRepositoryImpl implements GeographicZoneRepository {

    private final PropTech propTech;

    public GeographicZoneRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public GeographicZone save(GeographicZone zone) {
        propTech.getGeographicZones().add(zone);
        return zone;
    }

    @Override
    public Optional<GeographicZone> findById(String id) {
        for (int i = 0; i < propTech.getGeographicZones().size(); i++) {
            if (propTech.getGeographicZones().get(i).getId().equals(id))
                return Optional.of(propTech.getGeographicZones().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getGeographicZones().size(); i++) {
            if (propTech.getGeographicZones().get(i).getId().equals(id)) {
                propTech.getGeographicZones().remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public GeographicZone update(GeographicZone zone) {
        for (int i = 0; i < propTech.getGeographicZones().size(); i++) {
            if (propTech.getGeographicZones().get(i).getId().equals(zone.getId())) {
                propTech.getGeographicZones().set(i, zone);
            }
        }
        return zone;
    }

    @Override
    public ArrayList<GeographicZone> getGeographicZones() {
        return propTech.getGeographicZones();
    }

    @Override
    public Optional<GeographicZone> findByCityZoneNeighborhood(City city, Zone zone, String nameNeighborhood) {
        for (int i = 0; i < propTech.getGeographicZones().size(); i++) {
            GeographicZone gz = propTech.getGeographicZones().get(i);
            if (gz.getCity().equals(city) && gz.getZone().equals(zone) && gz.getNameNeighborhood().equals(nameNeighborhood))
                return Optional.of(gz);
        }
        return Optional.empty();
    }
}