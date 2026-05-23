package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.GeographicZoneDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.GeographicZoneRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeographicZoneService {

    GeographicZoneRepository geographicZoneRepository;

    public GeographicZoneService(GeographicZoneRepository geographicZoneRepository) {
        this.geographicZoneRepository = geographicZoneRepository;
    }

    public GeographicZone registerGeographicZone(GeographicZone geographicZone) {
        geographicZone.setId(CodeGenerator.generateZoneCode());
        return geographicZoneRepository.save(geographicZone);
    }

    public GeographicZone updateGeographicZone(GeographicZone geographicZone) {
        return geographicZoneRepository.findById(geographicZone.getId()).map(existing -> {
            Optional.ofNullable(geographicZone.getCity()).ifPresent(existing::setCity);
            Optional.ofNullable(geographicZone.getZone()).ifPresent(existing::setZone);
            Optional.ofNullable(geographicZone.getNameNeighborhood()).ifPresent(existing::setNameNeighborhood);
            return geographicZoneRepository.update(existing);
        }).orElseThrow(() -> new RuntimeException("No existe una zona geográfica con ese ID: " + geographicZone.getId()));
    }

    public void deleteGeographicZone(GeographicZone geographicZone) {
        if (geographicZoneRepository.findById(geographicZone.getId()).isEmpty()) {
            throw new GeographicZoneDoesNotExist("id", geographicZone.getId());
        }
        geographicZoneRepository.deleteById(geographicZone.getId());
    }

    public ArrayList<GeographicZone> getAllGeographicZones() {
        return geographicZoneRepository.getGeographicZones();
    }

    public GeographicZone getGeographicZoneById(String id) {
        return geographicZoneRepository.findById(id)
                .orElseThrow(() -> new GeographicZoneDoesNotExist("id", id));
    }

    public GeographicZone findOrCreate(GeographicZone geographicZone) {
        return geographicZoneRepository
                .findByCityZoneNeighborhood(geographicZone.getCity(), geographicZone.getZone(), geographicZone.getNameNeighborhood())
                .orElseGet(() -> {
                    geographicZone.setId(CodeGenerator.generateZoneCode());
                    return geographicZoneRepository.save(geographicZone);
                });
    }
}