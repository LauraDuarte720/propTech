package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    AgentRepository agentRepository;
    VisitService visitService;

    public AgentService(AgentRepository agentRepository, VisitService visitService) {
        this.agentRepository = agentRepository;
        this.visitService = visitService;
    }

    public Agent registerAgent(Agent agent) {
        boolean exists = agentRepository.findByCedula(agent.getCedula()).isPresent();
        if (exists) {
            throw new RuntimeException("An agent with this ID already exists");
        }
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent) {
        if (agentRepository.findByCedula(agent.getCedula()).isEmpty()) {
            throw new RuntimeException("No agent found with this ID");
        }
        return agentRepository.save(agent);
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

}