package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

import java.util.Optional;

public class PropertyRepositoryImpl implements PropertyRepository {

    private final PropTech propTech;

    public PropertyRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Property save(Property property) {
        return propTech.addUpdateProperty(property);
    }

    @Override
    public Optional<Property> findByCode(String code) {
        return Optional.ofNullable(propTech.getProperty(code));
    }

    @Override
    public boolean deleteById(String code) {
        return propTech.removeProperty(code);
    }

    @Override
    public HashTable<String, Property> getProperties() {
        return propTech.getProperties();
    }
}