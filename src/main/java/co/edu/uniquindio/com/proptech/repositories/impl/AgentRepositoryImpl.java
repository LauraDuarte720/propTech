package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class AgentRepositoryImpl implements AgentRepository {

    private final PropTech propTech;

    public AgentRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Agent save(Agent agent) {
        Agent existing = propTech.getAgents().get(agent.getCedula());
        if (existing != null) {
            propTech.getAgentsTree().delete(existing);
        }
        propTech.getAgents().put(agent.getCedula(), agent);
        propTech.getAgentsTree().insert(agent);
        return agent;
    }

    @Override
    public Optional<Agent> findByCedula(String cedula) {
        return Optional.ofNullable(propTech.getAgents().get(cedula));
    }

    @Override
    public boolean deleteById(String cedula) {
        Agent existing = propTech.getAgents().get(cedula);
        if (existing != null) {
            propTech.getAgentsTree().delete(existing);
        }
        return propTech.getAgents().remove(cedula);
    }

    @Override
    public HashTable<String, Agent> getAgents() {
        return propTech.getAgents();
    }

    @Override
    public AVLTree<Agent> getAgentsOrderedByClosedDeals() {
        return propTech.getAgentsTree();
    }
}