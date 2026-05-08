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

    @Override
    public Optional<Neighborhood> findByNameCityZone(String name, City city, Zone zone) {
        for (Neighborhood n : propTech.getNeighborhoods()) {
            if (n.getName().equals(name) && n.getCity().equals(city) && n.getZone().equals(zone)) {
                return Optional.of(n);
            }
        }
        return Optional.empty();
    }
}