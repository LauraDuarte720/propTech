package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.GeographicZoneRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class GeographicZoneRepositoryImpl implements GeographicZoneRepository {

    private PropTech propTech;

    public GeographicZoneRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public GeographicZone save(GeographicZone geographicZone) {
        return propTech.addGeographicZone(geographicZone);
    }

    @Override
    public Optional<GeographicZone> findById(String id) {
        return Optional.ofNullable(propTech.getGeographicZone(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeGeographicZone(id);
    }

    @Override
    public GeographicZone update(GeographicZone geographicZone) {
        return null;
    }

    @Override
    public ArrayList<GeographicZone> getGeographicZones() {
        return null;
    }
}
