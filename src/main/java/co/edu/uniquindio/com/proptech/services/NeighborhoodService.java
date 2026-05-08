package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NeighborhoodAlreadyExists;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NeighborhoodDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.NeighborhoodRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NeighborhoodService {

    NeighborhoodRepository neighborhoodRepository;

    public NeighborhoodService(NeighborhoodRepository neighborhoodRepository) {
        this.neighborhoodRepository = neighborhoodRepository;
    }

    public Neighborhood registerNeighborhood(Neighborhood neighborhood) {
        boolean exists = neighborhoodRepository.findById(neighborhood.getId()).isPresent();

        if (exists) {
            throw new NeighborhoodAlreadyExists("id", neighborhood.getId());
        }
        neighborhood.setId(CodeGenerator.generateNeighborCode());

        return neighborhoodRepository.save(neighborhood);
    }

    public Neighborhood updateNeighborhood(Neighborhood neighborhood) {
        return neighborhoodRepository.findById(neighborhood.getId()).map(existing -> {
            Optional.ofNullable(neighborhood.getCity()).ifPresent(existing::setCity);
            Optional.ofNullable(neighborhood.getZone()).ifPresent(existing::setZone);
            Optional.ofNullable(neighborhood.getName()).ifPresent(existing::setName);
            return neighborhoodRepository.update(existing);
        }).orElseThrow(() -> new NeighborhoodDoesNotExist("id", neighborhood.getId()));
    }

    public void deleteNeighborhood(String id) {
        if (neighborhoodRepository.findById(id).isEmpty()) {
            throw new NeighborhoodDoesNotExist("id", id);
        }
        neighborhoodRepository.deleteById(id);
    }

    public ArrayList<Neighborhood> getAllNeighborhoods() {
        return neighborhoodRepository.getNeighborhoods();
    }

    public Neighborhood getNeighborhoodById(String id) {
        return neighborhoodRepository.findById(id)
                .orElseThrow(() -> new NeighborhoodDoesNotExist("id", id));
    }

    public Neighborhood findOrCreate(Neighborhood neighborhood) {
        return neighborhoodRepository
                .findByNameCityZone(neighborhood.getName(), neighborhood.getCity(), neighborhood.getZone())
                .orElseGet(() -> neighborhoodRepository.save(neighborhood));
    }
}