package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findByCedula(String cedula);
    boolean deleteById(String cedula);
    HashTable<String, Client> getClients();
    AVLTree<Client> getClientsOrderedByBudget();
}