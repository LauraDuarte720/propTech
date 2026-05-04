package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.repositories.GeographicZoneRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class GeographicZoneService {

    GeographicZoneRepository geographicZoneRepository;

    public GeographicZone registerGeographicZone(GeographicZone geographicZone) {
        if (geographicZone == null) {
            throw new RuntimeException("La zona geográfica no puede ser nula");
        }

        geographicZone.setId(CodeGenerator.generateZoneCode());
        return geographicZoneRepository.save(geographicZone);
    }

    public GeographicZone updateGeographicZone(GeographicZone geographicZone) {
        if (geographicZoneRepository.findById(geographicZone.getId()).isEmpty()) {
            throw new RuntimeException("No existe una zona geográfica con ese ID");
        }

        return geographicZoneRepository.update(geographicZone);
    }

    public void deleteGeographicZone(GeographicZone geographicZone) {
        if (geographicZoneRepository.findById(geographicZone.getId()).isEmpty()) {
            throw new RuntimeException("No existe una zona geográfica con ese ID");
        }

        geographicZoneRepository.deleteById(geographicZone.getId());
    }

    public ArrayList<GeographicZone> getAllGeographicZones() {
        ArrayList<GeographicZone> zones = geographicZoneRepository.getGeographicZones();

        if (zones == null || zones.isEmpty()) {
            throw new RuntimeException("No hay zonas geográficas registradas");
        }

        return zones;
    }

    public GeographicZone getGeographicZoneById(String id) {
        return geographicZoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una zona geográfica con ese ID: " + id));
    }
}