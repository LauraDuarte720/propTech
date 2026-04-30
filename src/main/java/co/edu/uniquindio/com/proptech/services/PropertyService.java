package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.repositories.impl.PropertyRepositoryImpl;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;

public class PropertyService {

    PropertyRepositoryImpl propertyRepository;

    public void registerProperty(Property property) {
        boolean exists = propertyRepository.findByCode(property.getCode()).isPresent();

        if (exists) {
            throw new RuntimeException("Ya existe una propiedad con ese código");
        }
        property.setCode(CodeGenerator.generatePropertyCode(property.getPropertyType()));
        propertyRepository.save(property);
    }

    public void updateProperty(Property property) {
        if (propertyRepository.findByCode(property.getCode()).isEmpty()) {
            throw new RuntimeException("No existe una propiedad con ese código");
        }

        propertyRepository.save(property);
    }

    public void deleteProperty(Property property) {
        if (propertyRepository.findByCode(property.getCode()).isEmpty()) {
            throw new RuntimeException("No existe una propiedad con ese código");
        }

        propertyRepository.deleteById(property.getCode());

    }

    public HashTable<String, Property> getAllProperties() {
        HashTable<String, Property> properties = propertyRepository.getProperties();

        if (properties == null || properties.isEmpty()) {
            throw new RuntimeException("No hay propiedades registradas");
        }

        return properties;
    }
}
