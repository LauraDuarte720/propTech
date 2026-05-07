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
    PropertyMapper propertyMapper;

    public AgentService(AgentRepository agentRepository, VisitService visitService, PropertyMapper propertyMapper) {
        this.agentRepository = agentRepository;
        this.visitService = visitService;
        this.propertyMapper = propertyMapper;
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

        if (agent == null) {
            throw new RuntimeException("La visita no tiene un agente asignado");
        }

        agent.enqueueVisit(saved);
        return saved;
    }

    public PriorityQueue<Visit> getVisitsAgent(Agent agent) {
        if (agent == null) {
            throw new RuntimeException("El agente no puede ser nulo");
        }
        if (!agent.hasVisits()) {
            throw new RuntimeException("El agente no tiene visitas programadas");
        }
        return agent.getScheduledVisits();
    }

    public Property addProperty(Property property, Agent agent) {
        if (agent == null) {
            throw new RuntimeException("El agente no puede ser nulo");
        }
        if (property == null) {
            throw new RuntimeException("La propiedad no puede ser nula");
        }
        if (!match(agent.getAssignedZone(), property.getNeighborhood())) {
            throw new ZonesNotMatchingException("The zone of the agent assigned with this property does not match with the neighborhood");
        }
        property.setAgent(agent);
        return agent.addProperty(property);
    }

    public Property removeProperty(Property property) {
        property.setAgent(null);
        return property;
    }


    private ArrayList<Property> getIncompatibleProperties(Agent agent, GeographicZone geographicZone) {
        ArrayList<Property> incompatibles = new ArrayList<>();
        for (Property property : agent.getAssignedProperties()) {
            boolean matches = match(geographicZone, property.getNeighborhood());
            if (!matches) {
                incompatibles.add(property);
            }
        }
        return incompatibles;
    }

    public void updateGeographicZone(GeographicZone newGeographicZone, Agent agent, boolean confirm) {
        ArrayList<Property> incompatibleProperties = getIncompatibleProperties(agent, newGeographicZone);

        if (!confirm && !incompatibleProperties.isEmpty()) {
            List<AffectedPropertyDto> affectedProperties = List.of();
            for (Property property : incompatibleProperties) {
                affectedProperties.add(
                        propertyMapper.toSimpleDto(property)
                );
            }
            throw new ZoneChangeConflictException(
                    "If you make this change, the following properties will not have an assigned agent anymore", affectedProperties);
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

    private LinkedList<Agent> getAgentsMatching(Neighborhood neighborhood) {
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