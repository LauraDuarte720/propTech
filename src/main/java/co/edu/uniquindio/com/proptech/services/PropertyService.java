package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.*;
import co.edu.uniquindio.com.proptech.mappers.impl.PropertyMapper;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    PropertyRepository propertyRepository;
    AgentService agentService;
    NeighborhoodService neighborhoodService;
    PropertyAssignmentService propertyAssignmentService;
    VisitService visitService;
    AdminActionService adminActionService;
    ZoneMatcher zoneMatcher;
    PropertyMapper propertyMapper;

    public PropertyService(PropertyRepository propertyRepository, @Lazy AgentService agentService,
                           NeighborhoodService neighborhoodService,
                           @Lazy PropertyAssignmentService propertyAssignmentService,
                           VisitService visitService, AdminActionService adminActionService, ZoneMatcher zoneMatcher, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.agentService = agentService;
        this.neighborhoodService = neighborhoodService;
        this.propertyAssignmentService = propertyAssignmentService;
        this.visitService = visitService;
        this.adminActionService = adminActionService;
        this.zoneMatcher = zoneMatcher;

        this.propertyMapper = propertyMapper;
    }


    public void changePropertyState(Property property, PropertyStatus status) {
        if(propertyRepository.findByCode(property.getCode()).isEmpty()){
            property.setStatus(status);
            return;
        }
        ArrayList<Property> origin = propertyRepository.getPropertiesByStatus(property.getStatus());
        ArrayList<Property> destination = propertyRepository.getPropertiesByStatus(status);
        property.setStatus(status);
        origin.remove(property);
        destination.add(property);
    }

    public void changePropertyType(Property property, PropertyType type) {
        if(propertyRepository.findByCode(property.getCode()).isEmpty()){
            property.setPropertyType(type);
            return;
        }
        ArrayList<Property> origin = propertyRepository.getPropertiesByType(property.getPropertyType());
        ArrayList<Property> destination = propertyRepository.getPropertiesByType(type);
        property.setPropertyType(type);
        origin.remove(property);
        destination.add(property);
    }

    public void changePropertyNeighborhood(Property property, Neighborhood neighborhood) {
        if(propertyRepository.findByCode(property.getCode()).isEmpty()){
            property.setNeighborhood(neighborhood);
            return;
        }
        ArrayList<Property> origin = propertyRepository.getPropertiesByCity(property.getNeighborhood().getCity());
        ArrayList<Property> destination = propertyRepository.getPropertiesByCity(neighborhood.getCity());
        property.setNeighborhood(neighborhood);
        origin.remove(property);
        destination.add(property);
    }
    public Property registerProperty(Property property, String agentId, boolean confirm) {
        property.setCode(CodeGenerator.generatePropertyCode(property.getPropertyType()));
        Neighborhood resolved = neighborhoodService.findOrCreate(property.getNeighborhood());
        changePropertyNeighborhood(property, resolved);

        if (agentId == null && !confirm) {
            throw new NoAgentConfirmationException();
        }

        if (agentId != null) {
            changePropertyState(property, PropertyStatus.NEW);
            Property saved = propertyRepository.save(property);
            adminActionService.log(AdminActionType.CREATE, AdminEntityType.PROPERTY,
                    "Property created and assigned to agent " + agentId + " -> " + saved.getCode(),
                    "Admin", saved.getCode());
            return saved;
        } else {
            changePropertyState(property, PropertyStatus.INACTIVE);
            Property saved = propertyRepository.save(property);
            adminActionService.log(AdminActionType.CREATE, AdminEntityType.PROPERTY,
                    "Property created without agent -> " + saved.getCode(),
                    "Admin", saved.getCode());
            return saved;
        }
    }

    public Property publishProperty(String propertyCode) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));

        if (property.getAgent() == null) {
            throw new NoAgentConfirmationException();
        }
        changePropertyState(property, PropertyStatus.ACTIVE);
        return propertyRepository.save(property);
    }

    public Property registerAndPublishProperty(Property property, String agentId) {
        Property saved = registerProperty(property, agentId, false);
        return publishPropertyWithLog(saved.getCode());
    }

    public Property publishPropertyWithLog(String propertyCode) {
        Property property = publishProperty(propertyCode);
        adminActionService.log(AdminActionType.PUBLISH, AdminEntityType.PROPERTY,
                "Property published: " + propertyCode,
                "Admin", propertyCode);
        return property;
    }

    public Property unpublishProperty(String propertyCode) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        changePropertyState(property, PropertyStatus.INACTIVE);
        return propertyRepository.save(property);
    }

    public Property unpublishPropertyWithLog(String propertyCode) {
        Property property = unpublishProperty(propertyCode);
        adminActionService.log(AdminActionType.UNPUBLISH, AdminEntityType.PROPERTY,
                "Property unpublished: " + propertyCode,
                "Admin", propertyCode);
        return property;
    }

    public Property updateProperty(Property property, boolean confirm) {
        return propertyRepository.findByCode(property.getCode()).map(existing -> {

            boolean skipLog = false;
            existing.saveSnapshot();

            if (property.getAddress() != null) {
                existing.setAddress(property.getAddress());
            }
            if (property.getNeighborhood() != null) {
                updatePropertyNeighborhood(existing, property.getNeighborhood(), confirm);
                skipLog = true;
            }
            if (property.getPurpose() != null) {
                existing.setPurpose(property.getPurpose());
            }
            if (property.getPrice() != null) {
                PriceHistory record = PriceHistory.builder()
                        .oldPrice(existing.getPrice())
                        .newPrice(property.getPrice())
                        .changedAt(LocalDateTime.now())
                        .build();
                existing.getPriceHistory().addLast(record);
                existing.setPrice(property.getPrice());
            }
            if (property.getArea() != null) {
                existing.setArea(property.getArea());
            }
            if (property.getNumBedrooms() != null) {
                existing.setNumBedrooms(property.getNumBedrooms());
            }
            if (property.getNumBathrooms() != null) {
                existing.setNumBathrooms(property.getNumBathrooms());
            }
            if (property.getAgent() != null) {
                propertyAssignmentService.assignAgent(existing.getCode(), property.getAgent().getCedula());
                skipLog = true;
            }
            if(property.getPropertyType() != null) {
                changePropertyType(existing, property.getPropertyType());
            }

            Property saved = propertyRepository.save(existing);

            if (!skipLog) {
                adminActionService.log(
                        AdminActionType.UPDATE,
                        AdminEntityType.PROPERTY,
                        "Property updated: " + existing.getCode(),
                        "Admin",
                        existing.getCode()
                );
            }
            return saved;

        }).orElseThrow(() -> new PropertyDoesNotExist("code", property.getCode()));
    }

    private void updatePropertyNeighborhood(Property property, Neighborhood neighborhood, boolean confirm) {
        Neighborhood resolved = neighborhoodService.findOrCreate(neighborhood);
        Agent agent = property.getAgent();

        if (agent == null) {
            changePropertyNeighborhood(property, resolved);
            return;
        }

        boolean matches = zoneMatcher.match(agent.getAssignedZone(), resolved);

        if (!matches && !confirm) {
            throw new ZoneChangeConflictException(
                    "El agente asignado no cubre la nueva zona. Confirma para desasignarlo.",
                    List.of(propertyMapper.toSimpleDto(property))
            );
        }

        if (!matches) {
            propertyAssignmentService.removeAgentFromProperty(property.getCode(), agent.getCedula());
        }

        changePropertyNeighborhood(property, resolved);
    }

    public void deleteProperty(String propertyCode) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        propertyRepository.deleteById(propertyCode);
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

    public void assertPropertyVisitableAndRequestable(String propertyCode) {
        Property property = getPropertyByCode(propertyCode);
        PropertyStatus status = property.getStatus();
        if (status != PropertyStatus.ACTIVE && status != PropertyStatus.RESERVED) {
            throw new PropertyNotOperatableException(propertyCode, status);
        }
    }

    public void assertPropertyOperatable(String propertyCode, OperationType operationType) {
        Property property = getPropertyByCode(propertyCode);
        PropertyStatus status = property.getStatus();

        if (status == PropertyStatus.RENTED) {
            if (operationType != OperationType.CONTRACT_RENEWAL &&
                    operationType != OperationType.DEAL_CANCELLATION) {
                throw new PropertyNotOperatableException(propertyCode, status);
            }
            return;
        }

        if (status != PropertyStatus.ACTIVE) {
            throw new PropertyNotOperatableException(propertyCode, status);
        }
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

    public ArrayList<Property> getPublishableProperties() {
        ArrayList<Property> result = new ArrayList<>();
        for (Property property : propertyRepository.getProperties().values()) {
            if (property.isAvailable()) {
                result.add(property);
            }
        }
        return result;
    }

    public ArrayList<Property> getPropertiesByCity(City city){
        return propertyRepository.getPropertiesByCity(city);
    }

    public ArrayList<Property> getPropertiesByType(PropertyType propertyType) {
        return  propertyRepository.getPropertiesByType(propertyType);
    }

    public ArrayList<Property> getPropertiesByStatus(PropertyStatus propertyStatus) {
        return propertyRepository.getPropertiesByStatus(propertyStatus);
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