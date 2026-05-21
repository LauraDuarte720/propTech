package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

import java.util.Optional;

public interface AgentRepository {
    Agent save(Agent agent);
    Optional<Agent> findByCedula(String cedula);
    boolean deleteById(String cedula);
    HashTable<String, Agent> getAgents();
    AVLTree<Agent> getAgentsOrderedByClosedDeals();
    void deleteByCedula(String cedula);
}