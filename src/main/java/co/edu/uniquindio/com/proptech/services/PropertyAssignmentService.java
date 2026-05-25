package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.AgentDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.PropertyDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.ZonesNotMatchingException;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Service;

@Service
public class PropertyAssignmentService {
    private final ZoneMatcher zoneMatcher;
    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;
    private final PropertyService propertyService;

    public PropertyAssignmentService(ZoneMatcher zoneMatcher, PropertyRepository propertyRepository, AgentRepository agentRepository, PropertyService propertyService) {
        this.zoneMatcher = zoneMatcher;
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
        this.propertyService = propertyService;
    }

    public Property assignAgent(String propertyCode, String agentId) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        Agent agent = agentRepository.findByCedula(agentId)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", agentId));

        if (!zoneMatcher.match(agent.getAssignedZone(), property.getNeighborhood())) {
            throw new ZonesNotMatchingException("The zone of the agent does not match the neighborhood of the property");
        }

        property.setAgent(agent);
        agent.addProperty(property);
        return propertyRepository.save(property);
    }


    public Property removeAgentFromProperty(String propertyCode, String agentId) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        Agent agent = agentRepository.findByCedula(agentId)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", agentId));

        agent.removeProperty(property);
        property.removeAgent();

        PropertyStatus status = property.getStatus();
        boolean keepStatus = status == PropertyStatus.SOLD
                || status == PropertyStatus.RENTED
                || status == PropertyStatus.RESERVED;

        if (!keepStatus) {
            propertyService.changePropertyState(property, PropertyStatus.INACTIVE);
        }

        propertyRepository.save(property);
        return property;
    }

}