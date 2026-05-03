package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.NeighborhoodRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;

import java.util.Optional;

public class NeighborhoodRepositoryImpl implements NeighborhoodRepository {

    private final PropTech propTech;

    public NeighborhoodRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Neighborhood save(Neighborhood neighborhood) {
        return propTech.addNeighborhood(neighborhood);
    }

    @Override
    public Optional<Neighborhood> findById(String id) {
        return Optional.ofNullable(propTech.getNeighborhood(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeNeighborhood(id);
    }

    @Override
    public Neighborhood update(Neighborhood neighborhood) {
        return propTech.updateNeighborhood(neighborhood);
    }

    @Override
    public ArrayList<Neighborhood> getNeighborhoods() {
        return propTech.getNeighborhoods();
    }
}