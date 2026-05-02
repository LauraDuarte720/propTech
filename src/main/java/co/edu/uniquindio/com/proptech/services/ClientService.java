package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;

import java.time.LocalDateTime;

public class ClientService {

    ClientRepository clientRepository;

    public Client registerClient(Client client) {
        boolean exists = clientRepository.findByCedula(client.getCedula()).isPresent();
        if (exists) {
            throw new RuntimeException("A client with this ID already exists");
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        if (clientRepository.findByCedula(client.getCedula()).isEmpty()) {
            throw new RuntimeException("No client found with this ID");
        }
        return clientRepository.save(client);
    }

    public void deleteClient(Client client) {
        if (clientRepository.findByCedula(client.getCedula()).isEmpty()) {
            throw new RuntimeException("No client found with this ID");
        }
        clientRepository.deleteById(client.getCedula());
    }

    public HashTable<String, Client> getClients() {
        HashTable<String, Client> clients = clientRepository.getClients();

        if (clients == null || clients.isEmpty()) {
            throw new RuntimeException("No clients registered");
        }
        return clients;
    }

    public Client getClientByCedula(String cedula) {
        return clientRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("No client found with this ID: " + cedula));
    }

    public UserInteraction registerUserInteraction(Client client, UserInteraction userInteraction) {

        if (client == null) {
            throw new RuntimeException("Client cannot be null");
        }

        if (userInteraction == null) {
            throw new RuntimeException("Interaction cannot be null");
        }
        userInteraction.setId(CodeGenerator.generateInteractionCode());
        userInteraction.setTimestamp(LocalDateTime.now());
        userInteraction.setClient(client);
        client.addInteraction(userInteraction);
        return userInteraction;
    }

    public ArrayList<Property> getFavorites(Client client) {
        ArrayList<UserInteraction> saved = client.getInteractionsByType(InteractionType.SAVED);
        ArrayList<Property> favorites = new ArrayList<>();
        for (int i = 0; i < saved.size(); i++) {
            favorites.add(saved.get(i).getProperty());
        }
        if (favorites.isEmpty()) {
            throw new RuntimeException("The client has no saved properties");
        }
        return favorites;
    }


    public HashTable<InteractionType, ArrayList<UserInteraction>> getUserInteractions(Client client) {
        if (client == null) {
            throw new RuntimeException("Client cannot be null");
        }
        HashTable<InteractionType, ArrayList<UserInteraction>> interactions = client.getInteractionHistory();

        if (interactions == null || interactions.isEmpty()) {
            throw new RuntimeException("The client has no recorded interactions");
        }
        return interactions;
    }
}