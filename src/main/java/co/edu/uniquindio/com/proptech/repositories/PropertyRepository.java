package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Property;
import java.util.Optional;

public interface PropertyRepository {
    void save(Property property);
    Optional<Property> findByCode(String code);
    boolean deleteById(String code);
}