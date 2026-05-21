package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NoAgentConfirmationException;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.PropertyAlreadyExists;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.PropertyDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
public class PropertyService {

    PropertyRepository propertyRepository;
    AgentService agentService;
    NeighborhoodService neighborhoodService;
    PropertyAssignmentService propertyAssignmentService;
    VisitService visitService;
    AdminActionService adminActionService;

    public PropertyService(PropertyRepository propertyRepository, AgentService agentService,
                           NeighborhoodService neighborhoodService,
                           PropertyAssignmentService propertyAssignmentService,
                           VisitService visitService, AdminActionService adminActionService) {
        this.propertyRepository = propertyRepository;
        this.agentService = agentService;
        this.neighborhoodService = neighborhoodService;
        this.propertyAssignmentService = propertyAssignmentService;
        this.visitService = visitService;
        this.adminActionService = adminActionService;
    }

    public Property registerProperty(Property property, String agentId, boolean confirm) {

        boolean exists = propertyRepository.findByCode(property.getCode()).isPresent();
        if (exists) {
            throw new PropertyAlreadyExists("code", property.getCode());
        }

        Neighborhood resolved = neighborhoodService.findOrCreate(property.getNeighborhood());
        property.setNeighborhood(resolved);

        property.setCode(CodeGenerator.generatePropertyCode(property.getPropertyType()));

        if (agentId == null && !confirm) {
            throw new NoAgentConfirmationException();
        }

        Property saved;

        if (agentId != null) {
            property.setStatus(PropertyStatus.NEW);
            saved = propertyRepository.save(property);
            propertyAssignmentService.assignAgent(saved.getCode(), agentId);
            adminActionService.log(
                    AdminActionType.CREATE,
                    AdminEntityType.PROPERTY,
                    "Property created and assigned to agent " + agentId + " -> " + saved.getCode(),
                    "Admin"
            );
            return saved;
        } else {
            property.setStatus(PropertyStatus.INACTIVE);
            saved = propertyRepository.save(property);
            adminActionService.log(
                    AdminActionType.CREATE,
                    AdminEntityType.PROPERTY,
                    "Property created without agent -> " + saved.getCode(),
                    "Admin"
            );
            return saved;
        }
    }

    public Property publishProperty(String propertyCode) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));

        if (property.getAgent() == null) {
            throw new NoAgentConfirmationException();
        }
        property.setStatus(PropertyStatus.ACTIVE);
        Property saved = propertyRepository.save(property);

        adminActionService.log(
                AdminActionType.UPDATE,
                AdminEntityType.PROPERTY,
                "Property published: " + propertyCode,
                "Admin"
        );
        return saved;
    }

    public Property registerAndPublishProperty(Property property, String agentId) {
        Property saved = registerProperty(property, agentId, false);
        return publishProperty(saved.getCode());
    }

    public Property updateProperty(Property property) {
        return propertyRepository.findByCode(property.getCode()).map(existing -> {

            existing.saveSnapshot();

            Optional.ofNullable(property.getAddress()).ifPresent(existing::setAddress);
            Optional.ofNullable(property.getNeighborhood()).ifPresent(existing::setNeighborhood);
            Optional.ofNullable(property.getPurpose()).ifPresent(existing::setPurpose);

            Optional.ofNullable(property.getPrice()).ifPresent(newPrice -> {
                PriceHistory record = PriceHistory.builder()
                        .oldPrice(existing.getPrice())
                        .newPrice(newPrice)
                        .changedAt(LocalDateTime.now())
                        .build();
                existing.getPriceHistory().addLast(record);
                existing.setPrice(newPrice);
            });

            Optional.ofNullable(property.getArea()).ifPresent(existing::setArea);
            Optional.ofNullable(property.getNumBedrooms()).ifPresent(existing::setNumBedrooms);
            Optional.ofNullable(property.getNumBathrooms()).ifPresent(existing::setNumBathrooms);
            Optional.ofNullable(property.getStatus()).ifPresent(existing::setStatus);
            Optional.ofNullable(property.getAgent()).ifPresent(newAgent ->
                    propertyAssignmentService.assignAgent(existing.getCode(), newAgent.getCedula())
            );

            Property saved = propertyRepository.save(existing);

            adminActionService.log(
                    AdminActionType.UPDATE,
                    AdminEntityType.PROPERTY,
                    "Property updated: " + existing.getCode(),
                    "Admin"
            );

            return saved;

        }).orElseThrow(() -> new PropertyDoesNotExist("code", property.getCode()));
    }

    public void deleteProperty(String propertyId) {
        Property property = propertyRepository.findByCode(propertyId)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyId));

        propertyRepository.deleteById(propertyId);

        adminActionService.log(
                AdminActionType.DELETE,
                AdminEntityType.PROPERTY,
                "Property deleted: " + propertyId,
                "Admin"
        );
    }

    public HashTable<String, Property> getAllProperties() {
        return propertyRepository.getProperties();
    }

    public Property getPropertyByCode(String code) {
        return propertyRepository.findByCode(code)
                .orElseThrow(() -> new PropertyDoesNotExist("code", code));
    }

    public Property undoLastChange(String propertyCode) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));

        PropertySnapshot snapshot = property.getLastSnapshot();
        property.restoreSnapshot(snapshot);
        return property;
    }

    public AVLTree<Property> getPropertiesOrderedByPrice() {
        return propertyRepository.getPropertiesOrderedByPrice();
    }

    public ArrayList<Property> getPropertiesByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Los límites del rango no pueden ser nulos.");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Los precios no pueden ser negativos.");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al máximo.");
        }

        // Creamos Properties "fantasma" solo para usar como límites de comparación en el árbol
        Property minBound = Property.builder().price(minPrice).build();
        Property maxBound = Property.builder().price(maxPrice).build();

        AVLTree<Property> tree = getPropertiesOrderedByPrice();

        if (tree.isEmpty()) {
            throw new PropertyDoesNotExist("rango", minPrice + " - " + maxPrice);
        }

        ArrayList<Property> result = tree.rangeSearch(minBound, maxBound);

        if (result.isEmpty()) {
            throw new PropertyDoesNotExist("rango de precio", minPrice + " - " + maxPrice);
        }

        return result;
    }

    public HashTable<City, ArrayList<Property>> getPropertiesByCity() {
        return propertyRepository.getPropertiesByCity();
    }

    public HashTable<PropertyType, ArrayList<Property>> getPropertiesByType() {
        return propertyRepository.getPropertiesByType();
    }

    public HashTable<PropertyStatus, ArrayList<Property>> getPropertiesByStatus() {
        return propertyRepository.getPropertiesByStatus();
    }

    public ArrayList<Property> getPropertiesOrderedByArea() {
        ArrayList<Property> list = getAllPropertiesAsList();
        mergeSort(list, Comparator.comparingDouble(Property::getArea));
        return list;
    }

    public ArrayList<Property> getPropertiesOrderedByDemand() {
        HashTable<String, Integer> frequency = visitService.getFrequencyByProperty();
        ArrayList<Property> list = getAllPropertiesAsList();
        mergeSort(list, (a, b) -> {
            Integer freqA = frequency.get(a.getCode());
            Integer freqB = frequency.get(b.getCode());
            int fa = freqA == null ? 0 : freqA;
            int fb = freqB == null ? 0 : freqB;
            return Integer.compare(fb, fa); // mayor demanda primero
        });
        return list;
    }

    private ArrayList<Property> getAllPropertiesAsList() {
        ArrayList<Property> list = new ArrayList<>();
        for (Property property : propertyRepository.getProperties().values()) {
            list.add(property);
        }
        return list;
    }

    private void mergeSort(ArrayList<Property> list, Comparator<Property> comparator) {
        if (list.size() <= 1) return;
        int mid = list.size() / 2;

        ArrayList<Property> left = new ArrayList<>();
        ArrayList<Property> right = new ArrayList<>();

        for (int i = 0; i < mid; i++) left.add(list.get(i));
        for (int i = mid; i < list.size(); i++) right.add(list.get(i));

        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(list, left, right, comparator);
    }

    private void merge(ArrayList<Property> list, ArrayList<Property> left, ArrayList<Property> right, Comparator<Property> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }
}