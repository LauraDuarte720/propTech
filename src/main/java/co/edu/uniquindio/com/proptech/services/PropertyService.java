package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.NoAgentConfirmationException;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyService {

    PropertyRepository propertyRepository;
    AgentService agentService;
    NeighborhoodService neighborhoodService;
    PropertyAssignmentService propertyAssignmentService;

    public PropertyService(PropertyRepository propertyRepository, AgentService agentService, NeighborhoodService neighborhoodService, PropertyAssignmentService propertyAssignmentService) {
        this.propertyRepository = propertyRepository;
        this.agentService = agentService;
        this.neighborhoodService = neighborhoodService;
        this.propertyAssignmentService = propertyAssignmentService;
    }

    public Property registerProperty(Property property, String agentId, boolean confirm) {
        boolean exists = propertyRepository.findByCode(property.getCode()).isPresent();
        if (exists) {
            throw new RuntimeException("A property with this code already exists");
        }
        Neighborhood resolved = neighborhoodService.findOrCreate(property.getNeighborhood());
        property.setNeighborhood(resolved);
        property.setCode(CodeGenerator.generatePropertyCode(property.getPropertyType()));
        if (agentId == null && !confirm) {
            throw new NoAgentConfirmationException();
        }
        Property saved = propertyRepository.save(property);

        if (agentId != null) {
            propertyAssignmentService.assignAgent(saved.getCode(), agentId);
        }
        return saved;
    }

    public Property updateProperty(Property property) {
        return propertyRepository.findByCode(property.getCode()).map(existing -> {
            Optional.ofNullable(property.getAddress()).ifPresent(existing::setAddress);
            Optional.ofNullable(property.getNeighborhood()).ifPresent(existing::setNeighborhood);
            Optional.ofNullable(property.getPurpose()).ifPresent(existing::setPurpose);
            Optional.ofNullable(property.getPrice()).ifPresent(existing::setPrice);
            Optional.ofNullable(property.getArea()).ifPresent(existing::setArea);
            Optional.ofNullable(property.getNumBedrooms()).ifPresent(existing::setNumBedrooms);
            Optional.ofNullable(property.getNumBathrooms()).ifPresent(existing::setNumBathrooms);
            Optional.ofNullable(property.getStatus()).ifPresent(existing::setStatus);
            Optional.ofNullable(property.getAgent()).ifPresent(existing::setAgent);
            return propertyRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("No property found with this code: " + property.getCode()));
    }

    public void deleteProperty(String properyId) {
        if (propertyRepository.findByCode(properyId).isEmpty()) {
            throw new RuntimeException("No property found with this code");
        }
        propertyRepository.deleteById(properyId);
    }

    public HashTable<String, Property> getAllProperties() {
        HashTable<String, Property> properties = propertyRepository.getProperties();

        if (properties == null || properties.isEmpty()) {
            throw new RuntimeException("No properties registered");
        }

        return properties;
    }

    public Property getPropertyByCode(String code) {
        return propertyRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("No property found with this code"));
    }
}