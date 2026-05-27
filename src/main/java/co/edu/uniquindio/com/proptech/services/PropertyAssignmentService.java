package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.*;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.PropertyRepository;
import co.edu.uniquindio.com.proptech.repositories.OperationRepository;
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
    private final OperationRepository operationRepository;

    public PropertyAssignmentService(ZoneMatcher zoneMatcher, PropertyRepository propertyRepository, AgentRepository agentRepository, PropertyService propertyService, OperationRepository operationRepository) {
        this.zoneMatcher = zoneMatcher;
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
        this.propertyService = propertyService;
        this.operationRepository = operationRepository;
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

        // Validar visitas pendientes PARA ESTA PROPIEDAD
        if (hasActiveVisitsForProperty(agent, propertyCode)) {
            throw new AgentHasPendingVisitsException(agentId);
        }

        // Validar operaciones activas PARA ESTA PROPIEDAD
        if (hasActiveOperationsForProperty(agent, propertyCode)) {
            throw new AgentHasPendingOperationsException(agentId);
        }

        // Validar support requests pendientes PARA ESTA PROPIEDAD
        if (hasPendingSupportRequestsForProperty(agent, propertyCode)) {
            throw new AgentHasPendingSupportRequestsException(agentId);
        }

        // Validar alertas no revisadas PARA ESTA PROPIEDAD
        if (agent.hasAlertsForProperty(propertyCode)) {
            throw new AgentHasPendingAlertsException(agentId, propertyCode);
        }

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

    private boolean hasActiveVisitsForProperty(Agent agent, String propertyCode) {
        for (Visit v : agent.getScheduledVisits()) {
            if (v.getProperty() != null && v.getProperty().getCode().equals(propertyCode)
                && v.getStatus() != VisitStatus.CANCELED
                && v.getStatus() != VisitStatus.COMPLETED
                && v.getStatus() != VisitStatus.EXPIRED) {
                return true;
            }
        }
        return false;
    }

    private boolean hasActiveOperationsForProperty(Agent agent, String propertyCode) {
        for (Operation op : operationRepository.getOperationsByProperty(propertyCode)) {
            if (op.getAgent() != null && op.getAgent().getCedula().equals(agent.getCedula())
                && op.getProcessStatus() == ProcessStatus.CREATED) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPendingSupportRequestsForProperty(Agent agent, String propertyCode) {
        for (SupportRequest sr : agent.getSupportRequests()) {
            if (sr.getProperty() != null && sr.getProperty().getCode().equals(propertyCode)
                && sr.getStatus() == SupportRequestStatus.PENDING) {
                return true;
            }
        }
        return false;
    }

}