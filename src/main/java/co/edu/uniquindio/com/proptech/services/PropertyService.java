package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;

public class PropertyService {

    PropertyRepository propertyRepository;

    public Property registerProperty(Property property) {
        boolean exists = propertyRepository.findByCode(property.getCode()).isPresent();

        if (exists) {
            throw new RuntimeException("Ya existe una propiedad con ese código");
        }
        property.setCode(CodeGenerator.generatePropertyCode(property.getPropertyType()));
        return propertyRepository.save(property);
    }

    public Property updateProperty(Property property) {
        if (propertyRepository.findByCode(property.getCode()).isEmpty()) {
            throw new RuntimeException("No existe una propiedad con ese código");
        }

       return propertyRepository.save(property);
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

    public Property getPropertyByCode(String code) {
        return propertyRepository.findByCode(code).orElseThrow(() -> new RuntimeException("No existe una propiedad con ese codigo"));
    }
}
