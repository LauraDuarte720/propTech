package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
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
            removeFromGroupingTables(existing); // limpia tablas de agrupación
        }

        propTech.getProperties().put(property.getCode(), property);
        propTech.getPropertiesTree().insert(property);
        addToGroupingTables(property); // agrega a tablas de agrupación
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
            removeFromGroupingTables(existing);
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
    private void addToGroupingTables(Property property) {
        City city         = property.getNeighborhood().getCity();
        PropertyType type = property.getPropertyType();
        PropertyStatus status = property.getStatus();

        addToGroup(propTech.getPropertiesByCity(),   city,   property);
        addToGroup(propTech.getPropertiesByType(),   type,   property);
        addToGroup(propTech.getPropertiesByStatus(), status, property);
    }

    private void removeFromGroupingTables(Property property) {
        City city             = property.getNeighborhood().getCity();
        PropertyType type     = property.getPropertyType();
        PropertyStatus status = property.getStatus();

        removeFromGroup(propTech.getPropertiesByCity(),   city,   property);
        removeFromGroup(propTech.getPropertiesByType(),   type,   property);
        removeFromGroup(propTech.getPropertiesByStatus(), status, property);
    }

    private <K> void addToGroup(HashTable<K, ArrayList<Property>> table, K key, Property property) {
        ArrayList<Property> list = table.get(key);
        if (list == null) {
            list = new ArrayList<>();
            table.put(key, list);
        }
        list.add(property);
    }

    private <K> void removeFromGroup(HashTable<K, ArrayList<Property>> table, K key, Property property) {
        ArrayList<Property> list = table.get(key);
        if (list != null) {
            list.remove(property);
        }
    }

    @Override
    public HashTable<City, ArrayList<Property>> getPropertiesByCity() {
        return propTech.getPropertiesByCity();
    }

    @Override
    public HashTable<PropertyType, ArrayList<Property>> getPropertiesByType() {
        return propTech.getPropertiesByType();
    }

    @Override
    public HashTable<PropertyStatus, ArrayList<Property>> getPropertiesByStatus() {
        return propTech.getPropertiesByStatus();
    }

    @Override
    public ArrayList<Property> getPropertiesByCity(City city){
        return getPropertiesByCity().get(city);
    }

    @Override
    public ArrayList<Property> getPropertiesByType(PropertyType propertyType) {
        return  getPropertiesByType().get(propertyType);
    }

    @Override
    public ArrayList<Property> getPropertiesByStatus(PropertyStatus propertyStatus) {
        return getPropertiesByStatus().get(propertyStatus);
    }
}