package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.config.mappers.impl.PropertyMapper;
import co.edu.uniquindio.com.proptech.domain.dtos.AffectedPropertyDto;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.AgentAlreadyExists;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.AgentDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.ZoneChangeConflictException;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.ZonesNotMatchingException;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    AgentRepository agentRepository;
    VisitService visitService;
    PropertyService propertyService;
    PropertyMapper propertyMapper;
    PropertyAssignmentService propertyAssignmentService;

    public AgentService(AgentRepository agentRepository, VisitService visitService, PropertyMapper propertyMapper, PropertyService propertyService, PropertyAssignmentService propertyAssignmentService) {
        this.agentRepository = agentRepository;
        this.visitService = visitService;
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
        this.propertyAssignmentService = propertyAssignmentService;
    }

    public Agent registerAgent(Agent agent) {
        boolean exists = agentRepository.findByCedula(agent.getCedula()).isPresent();
        if (exists) {
            throw new AgentAlreadyExists("cedula", agent.getCedula());
        }
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent) {
        return agentRepository.findByCedula(agent.getCedula()).map(existing -> {
            Optional.ofNullable(agent.getName()).ifPresent(existing::setName);
            Optional.ofNullable(agent.getUsername()).ifPresent(existing::setUsername);
            Optional.ofNullable(agent.getContact()).ifPresent(existing::setContact);
            Optional.ofNullable(agent.getClosedDeals()).ifPresent(existing::setClosedDeals);
            return agentRepository.save(existing);
        }).orElseThrow(() -> new AgentDoesNotExist("cedula", agent.getCedula()));
    }

    public HashTable<String, Agent> getAgents() {
        HashTable<String, Agent> agents = agentRepository.getAgents();
        if (agents == null || agents.isEmpty()) {
            throw new RuntimeException("No agents registered");
        }
        return agents;
    }

    public Agent getAgentByCedula(String cedula) {
        return agentRepository.findByCedula(cedula)
                .orElseThrow(() -> new AgentDoesNotExist("cedula", cedula));
    }

    public Visit registerVisit(Visit visit) {
        Visit saved = visitService.registerVisit(visit);
        Agent agent = saved.getAgent();
        agent.enqueueVisit(saved);
        return saved;
    }

    public PriorityQueue<Visit> getVisitsAgent(String idAgent) {
        Agent agent = getAgentByCedula(idAgent);
        return agent.getScheduledVisits();
    }

    public Property addPropertyToAgent(String propertyCode, String agentId) {
        return propertyAssignmentService.assignAgent(propertyCode, agentId);
    }

    public Property removePropertyFromAgent(String propertyCode, String agentId) {
        return propertyAssignmentService.removeAgent(propertyCode, agentId);
    }

    private ArrayList<Property> getIncompatibleProperties(Agent agent, GeographicZone geographicZone) {
        ArrayList<Property> incompatibles = new ArrayList<>();
        for (Property property : agent.getAssignedProperties()) {
            if (!propertyAssignmentService.match(geographicZone, property.getNeighborhood())) {
                incompatibles.add(property);
            }
        }
        return incompatibles;
    }

    public void updateGeographicZone(GeographicZone newGeographicZone, Agent agent, boolean confirm) {
        ArrayList<Property> incompatibleProperties = getIncompatibleProperties(agent, newGeographicZone);

        if (!confirm && !incompatibleProperties.isEmpty()) {
            ArrayList<AffectedPropertyDto> affectedProperties = new ArrayList<>();
            for (Property property : incompatibleProperties) {
                affectedProperties.add(
                        propertyMapper.toSimpleDto(property)
                );
            }
            throw new ZoneChangeConflictException(
                    "If you make this change, the following properties will not have an assigned agent anymore", (List) affectedProperties);
        }
        agent.setAssignedZone(newGeographicZone);
        if (confirm) {
            for (Property property : incompatibleProperties) {
                property.setAgent(null);
            }
        }
        agentRepository.save(agent);
    }


    private boolean match(GeographicZone zone, Neighborhood propertyNeighborhood) {
        if (propertyNeighborhood == null) {
            return false;
        }

        if (zone == null) {
            return true;
        }

        if (!zone.getCity().equals(propertyNeighborhood.getCity())) {
            return false;
        }

        if (zone.getZone() != null && !zone.getZone().equals(propertyNeighborhood.getZone())) {
            return false;
        }

        if (zone.getNeighborhood() != null && !zone.getNeighborhood().equals(propertyNeighborhood.getName())) {
            return false;
        }

        return true;
    }

    public LinkedList<Agent> getAgentsMatchingNeighbor(Neighborhood neighborhood) {
        LinkedList<Agent> matching = new LinkedList<>();
        HashTable<String, Agent> agents = agentRepository.getAgents();
        for (Agent agent : agents.values()) {
            if (match(agent.getAssignedZone(), neighborhood)) {
                matching.addLast(agent);
            }
        }
        return matching;
    }

}