package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class PropertyRepositoryImpl implements PropertyRepository {

    private final PropTech propTech;

    public PropertyRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Property save(Property property) {
        Property existing = propTech.getProperties().get(property.getCode());
        if (existing != null) {
            propTech.getPropertiesTree().delete(existing);
        }

        propTech.getProperties().put(property.getCode(), property);
        propTech.getPropertiesTree().insert(property);
        return property;
    }

    @Override
    public Optional<Property> findByCode(String code) {
        return Optional.ofNullable(propTech.getProperties().get(code));
    }

    @Override
    public boolean deleteById(String code) {
        Property existing = propTech.getProperties().get(code);
        if (existing != null) {
            propTech.getPropertiesTree().delete(existing);
        }
        return propTech.getProperties().remove(code);
    }

    @Override
    public HashTable<String, Property> getProperties() {
        return propTech.getProperties();
    }

    @Override
    public AVLTree<Property> getPropertiesOrderedByPrice() {
        return propTech.getPropertiesTree();
    }
}