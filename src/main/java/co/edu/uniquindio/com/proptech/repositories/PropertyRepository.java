package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

import java.util.Optional;

public interface PropertyRepository {
    Property save(Property property);
    Optional<Property> findByCode(String code);
    boolean deleteById(String code);
    HashTable<String, Property> getProperties();
    AVLTree<Property> getPropertiesOrderedByPrice();
    HashTable<City, ArrayList<Property>> getPropertiesByCity();
    HashTable<PropertyType, ArrayList<Property>> getPropertiesByType();
    HashTable<PropertyStatus, ArrayList<Property>> getPropertiesByStatus();
}