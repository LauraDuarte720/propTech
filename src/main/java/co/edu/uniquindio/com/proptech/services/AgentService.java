package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

public class AgentService {

    AgentRepository agentRepository;

    public Agent registerAgent(Agent agent) {
        boolean exists = agentRepository.findByCedula(agent.getCedula()).isPresent();
        if (exists) {
            throw new RuntimeException("Ya existe un agente con esa cédula");
        }
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Agent agent) {
        if (agentRepository.findByCedula(agent.getCedula()).isEmpty()) {
            throw new RuntimeException("No existe un agente con esa cédula");
        }
        return agentRepository.save(agent);
    }



    public HashTable<String, Agent> getAgents() {
        HashTable<String, Agent> agents = agentRepository.getAgents();
        if (agents == null || agents.isEmpty()) {
            throw new RuntimeException("No hay agentes registrados");
        }
        return agents;
    }

    public Agent getAgentByCedula(String cedula) {
        return agentRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("No existe un agente con esa cédula: " + cedula));
    }
}
