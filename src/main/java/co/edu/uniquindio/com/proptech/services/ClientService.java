package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ClientService {

    ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(Client client) {
        boolean exists = clientRepository.findByCedula(client.getCedula()).isPresent();
        if (exists) {
            throw new RuntimeException("A client with this ID already exists");
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        return clientRepository.findByCedula(client.getCedula()).map(existing -> {
            Optional.ofNullable(client.getEmail()).ifPresent(existing::setEmail);
            Optional.ofNullable(client.getPhone()).ifPresent(existing::setPhone);
            Optional.ofNullable(client.getBudget()).ifPresent(existing::setBudget);
            Optional.ofNullable(client.getMinBedrooms()).ifPresent(existing::setMinBedrooms);
            Optional.ofNullable(client.getClientType()).ifPresent(existing::setClientType);
            Optional.ofNullable(client.getSearchStatus()).ifPresent(existing::setSearchStatus);
            Optional.ofNullable(client.getDesiredPropertyType()).ifPresent(existing::setDesiredPropertyType);
            Optional.ofNullable(client.getInterestZones()).ifPresent(existing::setInterestZones);
            return clientRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("No client found with this ID: " + client.getCedula()));
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