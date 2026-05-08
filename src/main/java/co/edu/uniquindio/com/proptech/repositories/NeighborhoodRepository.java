package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;

import java.util.Optional;

public interface NeighborhoodRepository {
    Neighborhood save(Neighborhood neighborhood);
    Optional<Neighborhood> findById(String id);
    boolean deleteById(String id);
    Neighborhood update(Neighborhood neighborhood);
    ArrayList<Neighborhood> getNeighborhoods();
    Optional<Neighborhood> findByNameCityZone(String name, City city, Zone zone);
}