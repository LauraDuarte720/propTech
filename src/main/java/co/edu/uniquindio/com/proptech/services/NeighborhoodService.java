package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.repositories.NeighborhoodRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class NeighborhoodService {

    NeighborhoodRepository neighborhoodRepository;

    public NeighborhoodService(NeighborhoodRepository neighborhoodRepository) {
        this.neighborhoodRepository = neighborhoodRepository;
    }

    public Neighborhood registerNeighborhood(Neighborhood neighborhood) {
        boolean exists = neighborhoodRepository.findById(neighborhood.getId()).isPresent();

        if (exists) {
            throw new RuntimeException("A neighborhood with this ID already exists");
        }

        return neighborhoodRepository.save(neighborhood);
    }

    public Neighborhood updateNeighborhood(Neighborhood neighborhood) {
        if (neighborhoodRepository.findById(neighborhood.getId()).isEmpty()) {
            throw new RuntimeException("No neighborhood found with this ID");
        }

        return neighborhoodRepository.update(neighborhood);
    }

    public void deleteNeighborhood(String id) {
        if (neighborhoodRepository.findById(id).isEmpty()) {
            throw new RuntimeException("No neighborhood found with this ID");
        }

        neighborhoodRepository.deleteById(id);
    }

    public ArrayList<Neighborhood> getAllNeighborhoods() {
        ArrayList<Neighborhood> neighborhoods = neighborhoodRepository.getNeighborhoods();

        if (neighborhoods == null || neighborhoods.isEmpty()) {
            throw new RuntimeException("No neighborhoods registered");
        }

        return neighborhoods;
    }

    public Neighborhood getNeighborhoodById(String id) {
        return neighborhoodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No neighborhood found with this ID: " + id));
    }
}