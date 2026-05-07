package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.config.mappers.impl.PropertyMapper;
import co.edu.uniquindio.com.proptech.domain.dtos.AffectedPropertyDto;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.validators.FieldErrorDetail;
import co.edu.uniquindio.com.proptech.utils.validators.LocationValidator;
import co.edu.uniquindio.com.proptech.utils.validators.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgentService {

    AgentRepository agentRepository;
    VisitService visitService;
    LocationValidator locationValidator;
    PropertyMapper propertyMapper;

    public AgentService(AgentRepository agentRepository, VisitService visitService, LocationValidator locationValidator, PropertyMapper propertyMapper) {
        this.agentRepository = agentRepository;
        this.visitService = visitService;
        this.locationValidator = locationValidator;
        this.propertyMapper = propertyMapper;
    }

    public Agent registerAgent(Agent agent) {
        boolean exists = agentRepository.findByCedula(agent.getCedula()).isPresent();
        if (exists) {
            throw new RuntimeException("An agent with this ID already exists");
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
        }).orElseThrow(() -> new RuntimeException("No agent found with this ID: " + agent.getCedula()));
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
                .orElseThrow(() -> new RuntimeException("No agent found with this ID: " + cedula));
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
        match(property, agent);
        property.setAgent(agent);
        return agent.addProperty(property);
    }


    private ArrayList<Property> getIncompatibleProperties(Agent agent, GeographicZone geographicZone) {
        ArrayList<Property> incompatible = new ArrayList<>();
        for (Property property : agent.getAssignedProperties()) {
            ValidationResult result = locationValidator.validate(
                    geographicZone,
                    property.getNeighborhood()
            );
            if (result.hasErrors()) {
                incompatible.add(property);
            }
        }
        return incompatible;
    }

    public void updateGeographicZone(GeographicZone geographicZone, Agent agent, boolean confirm) {
        ArrayList<Property> incompatibleProperties = getIncompatibleProperties(agent, geographicZone);

        if (!confirm && !incompatibleProperties.isEmpty()) {
            ArrayList<AffectedPropertyDto> affectedProperties = new ArrayList<>();
            for (Property property : incompatibleProperties) {
                affectedProperties.add(
                        propertyMapper.toSimpleDto(property)
                );
            }
            throw new RuntimeException(
                    "Si realizas este cambio, las siguientes propiedades quedarán sin agente asignado"
                   //affectedProperties
            );
        }
        agent.setAssignedZone(geographicZone);
        if (confirm) {
            for (Property property : incompatibleProperties) {
                property.setAgent(null);
            }
        }
        agentRepository.save(agent);
    }


    private void match(Property property, Agent agent) {

        ValidationResult result = locationValidator.validate(
                agent.getAssignedZone(),
                property.getNeighborhood()
        );

        if (result.hasErrors()) {
           // throw new RuntimeException(result.getErrors());
        }
    }
}