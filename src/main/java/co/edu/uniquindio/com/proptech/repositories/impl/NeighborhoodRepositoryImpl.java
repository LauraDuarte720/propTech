package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.NeighborhoodRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class NeighborhoodRepositoryImpl implements NeighborhoodRepository {

    private final PropTech propTech;

    public NeighborhoodRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Neighborhood save(Neighborhood neighborhood) {
        propTech.getNeighborhoods().add(neighborhood);
        return neighborhood;
    }

    @Override
    public Optional<Neighborhood> findById(String id) {
        for (int i = 0; i < propTech.getNeighborhoods().size(); i++) {
            if (propTech.getNeighborhoods().get(i).getId().equals(id))
                return Optional.of(propTech.getNeighborhoods().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getNeighborhoods().size(); i++) {
            if (propTech.getNeighborhoods().get(i).getId().equals(id)) {
                propTech.getNeighborhoods().remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Neighborhood update(Neighborhood neighborhood) {
        for (int i = 0; i < propTech.getNeighborhoods().size(); i++) {
            if (propTech.getNeighborhoods().get(i).getId().equals(neighborhood.getId())) {
                propTech.getNeighborhoods().set(i, neighborhood);
            }
        }
        return neighborhood;
    }

    @Override
    public ArrayList<Neighborhood> getNeighborhoods() {
        return propTech.getNeighborhoods();
    }

    @Override
    public Optional<Neighborhood> findByNameCityZone(String name, City city, Zone zone) {
        for (int i = 0; i < propTech.getNeighborhoods().size(); i++) {
            Neighborhood n = propTech.getNeighborhoods().get(i);
            if (n.getName().equals(name) && n.getCity().equals(city) && n.getZone().equals(zone))
                return Optional.of(n);
        }
        return Optional.empty();
    }
}