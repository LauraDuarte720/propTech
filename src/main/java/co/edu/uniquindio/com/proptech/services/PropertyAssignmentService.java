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
import org.springframework.stereotype.Service;

@Service
public class PropertyAssignmentService {
    private final ZoneMatcher zoneMatcher;
    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;
    private  final AdminActionService adminActionService;

    public PropertyAssignmentService(ZoneMatcher zoneMatcher, PropertyRepository propertyRepository, AgentRepository agentRepository, AdminActionService adminActionService) {
        this.zoneMatcher = zoneMatcher;
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
        this.adminActionService = adminActionService;
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
        adminActionService.logAssign(
                "Agent " + agentId + " assigned to property " + propertyCode,
                "Admin", propertyCode, agentId);
        return propertyRepository.save(property);
    }

    public Property removeAgentFromProperty(String propertyCode, String agentId) {
        Property property = propertyRepository.findByCode(propertyCode)
                .orElseThrow(() -> new PropertyDoesNotExist("code", propertyCode));
        Agent agent = agentRepository.findByCedula(agentId)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", agentId));

        agent.removeProperty(property);
        property.setAgent(null);
        property.setStatus(PropertyStatus.INACTIVE);
        propertyRepository.save(property);
        return property;
    }

    public Property removeAgentFromPropertyWithLog(String propertyCode, String agentId) {
        Property property = removeAgentFromProperty(propertyCode, agentId);
        adminActionService.logUnassign(
                "Agent " + agentId + " removed from property " + propertyCode,
                "Admin", propertyCode, agentId);
        return property;
    }

    // restore — sin log, usando la misma lógica pura al revés
    public void restorePropertiesAfterZoneUndo(Agent agent, ArrayList<String> propertyCodes) {
        for (String code : propertyCodes) {
            propertyRepository.findByCode(code).ifPresent(property -> {
                property.setAgent(agent);
                property.setStatus(PropertyStatus.ACTIVE);
                agent.addProperty(property);
                propertyRepository.save(property);
            });
        }
    }

}