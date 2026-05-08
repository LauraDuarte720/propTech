package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.AgentDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.PropertyDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.ZonesNotMatchingException;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.springframework.stereotype.Service;

@Service
public class PropertyAssignmentService {

    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;

    public PropertyAssignmentService(PropertyRepository propertyRepository, AgentRepository agentRepository) {
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
    }

    public Property assignAgent(String propertyCode, String agentId) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        Agent agent = agentRepository.findByCedula(agentId)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", agentId));

        if (!ZoneMatcher.match(agent.getAssignedZone(), property.getNeighborhood())) {
            throw new ZonesNotMatchingException("The zone of the agent does not match the neighborhood of the property");
        }

        property.setAgent(agent);
        agent.addProperty(property);
        return propertyRepository.save(property);
    }

    public Property removeAgent(String propertyCode, String agentId) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        Agent agent = agentRepository.findByCedula(agentId)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", agentId));

        agent.removeProperty(property);
        property.setAgent(null);
        return propertyRepository.save(property);
    }

}