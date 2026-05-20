package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.mappers.impl.PropertyMapper;
import co.edu.uniquindio.com.proptech.domain.dtos.AffectedPropertyDto;
import co.edu.uniquindio.com.proptech.domain.enums.SupportRequestStatus;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.*;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.ZoneMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    AgentRepository agentRepository;
    VisitService visitService;
    PropertyMapper propertyMapper;
    PropertyAssignmentService propertyAssignmentService;
    ZoneMatcher zoneMatcher;
    StructuresMappers structuresMappers;

    public AgentService(AgentRepository agentRepository, VisitService visitService, PropertyMapper propertyMapper, PropertyAssignmentService propertyAssignmentService, ZoneMatcher zoneMatcher, StructuresMappers structuresMappers) {
        this.agentRepository = agentRepository;
        this.visitService = visitService;
        this.propertyMapper = propertyMapper;
        this.propertyAssignmentService = propertyAssignmentService;
        this.zoneMatcher = zoneMatcher;
        this.structuresMappers = structuresMappers;
    }

    public Agent registerAgent(Agent agent) {
        boolean exists = agentRepository.findByCedula(agent.getCedula()).isPresent();
        if (exists) {
            throw new AgentAlreadyExists("cedula", agent.getCedula());
        }
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent, boolean confirm) {
        return agentRepository.findByCedula(agent.getCedula()).map(existing -> {
            Optional.ofNullable(agent.getName()).ifPresent(existing::setName);
            Optional.ofNullable(agent.getUsername()).ifPresent(existing::setUsername);
            Optional.ofNullable(agent.getContact()).ifPresent(existing::setContact);
            Optional.ofNullable(agent.getClosedDeals()).ifPresent(existing::setClosedDeals);
            Optional.ofNullable(agent.getAssignedZone())
                    .ifPresent(zone -> updateGeographicZone(zone, existing, confirm));
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
            if (!zoneMatcher.match(geographicZone, property.getNeighborhood())) {
                incompatibles.add(property);
            }
        }
        return incompatibles;
    }

    public void updateGeographicZone(GeographicZone newGeographicZone, Agent agent, boolean confirm) {
        if (agent.hasVisits()) {
            throw new AgentHasPendingVisitsException(agent.getCedula());
        }
        ArrayList<Property> incompatibleProperties = getIncompatibleProperties(agent, newGeographicZone);

        if (!confirm && !incompatibleProperties.isEmpty()) {
            throw new ZoneChangeConflictException(
                    "If you make this change, the following properties will not have an assigned agent anymore",
                    structuresMappers.fromArrayList(incompatibleProperties, propertyMapper::toSimpleDto)
            );
        }

        agent.setAssignedZone(newGeographicZone);
        if (confirm) {
            for (Property property : incompatibleProperties) {
                property.setAgent(null);
                property.setStatus(PropertyStatus.INACTIVE);
            }
        }
        agentRepository.save(agent);
    }

    public LinkedList<Agent> getAgentsMatchingNeighbor(Neighborhood neighborhood) {
        LinkedList<Agent> matching = new LinkedList<>();
        HashTable<String, Agent> agents = agentRepository.getAgents();
        for (Agent agent : agents.values()) {
            if (zoneMatcher.match(agent.getAssignedZone(), neighborhood)) {
                matching.addLast(agent);
            }
        }
        return matching;
    }

    public SupportRequest registerSupportRequest(SupportRequest request) {
        Agent agent = request.getAgent();
        agent.enqueueSupportRequest(request);
        agentRepository.save(agent);
        return request;
    }

    public SupportRequest getNextSupportRequest(String agentId) {
        Agent agent = getAgentByCedula(agentId);
        return agent.peekNextSupportRequest();
    }

    public SupportRequest attendSupportRequest(String agentId) {
        Agent agent = getAgentByCedula(agentId);
        SupportRequest request = agent.dequeueSupportRequest();
        if (request != null) {
        request.setStatus(SupportRequestStatus.ATTENDED);
    }
        agentRepository.save(agent);
        return request;
    }

    public ArrayList<Agent> getAgentsOrderedByClosedDeals() {
        AVLTree<Agent> tree = agentRepository.getAgentsOrderedByClosedDeals();

        if (tree.isEmpty()) {
            throw new RuntimeException("No hay asesores registrados.");
        }


        ArrayList<Agent> ordered = tree.inOrder();
        ArrayList<Agent> result = new ArrayList<>();
        for (int i = ordered.size() - 1; i >= 0; i--) {
            result.add(ordered.get(i));
        }
        return result;
    }
}