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
            throw new RuntimeException("Ya existe un cliente con esa cédula");
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {
        if  (clientRepository.findByCedula(client.getCedula()).isEmpty()) {
            throw new RuntimeException("No existe un cliente con esa cédula");
        }
        return clientRepository.save(client);
    }

    public void deleteClient(Client client) {
        if  (clientRepository.findByCedula(client.getCedula()).isEmpty()) {
            throw new RuntimeException("No existe un cliente con esa cédula");
        }
        clientRepository.deleteById(client.getCedula());
    }

    public HashTable<String, Client> getClients() {
        HashTable<String,Client> clients = clientRepository.getClients();

        if (clients == null || clients.isEmpty()) {
            throw new RuntimeException("No hay clientes registrados");

        }
        return clients;
    }

    public Client getClientByCedula(String cedula) {
        return clientRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("No existe un cliente con esa cédula: " + cedula));
    }

    public UserInteraction registerUserInteraction(Client client, UserInteraction userInteraction) {
        if (client == null) {
            throw new RuntimeException("El cliente no puede ser nulo");
        }

        if (userInteraction == null) {
            throw new RuntimeException("La interacción no puede ser nula");
        }

        if (userInteraction.getInteractionType() == null) {
            throw new RuntimeException("El tipo de interacción no puede ser nulo");
        }

        if (userInteraction.getProperty() == null) {
            throw new RuntimeException("La propiedad de la interacción no puede ser nula");
        }

        userInteraction.setId(CodeGenerator.generateInteractionCode());
        userInteraction.setClient(client);
        userInteraction.setTimestamp(LocalDateTime.now());
        client.addInteraction(userInteraction);

        return userInteraction;
    }

    public ArrayList<Property> getFavorites(Client client) {
        ArrayList<UserInteraction> interactions = client.getInteractionHistory();
        ArrayList<Property> favorites = new ArrayList<>();

        for (int i = 0; i < interactions.size(); i++) {
            UserInteraction interaction = interactions.get(i);
            if (interaction.getInteractionType() == InteractionType.SAVED) {
                favorites.add(interaction.getProperty());
            }
        }

        if (favorites.isEmpty()) {
            throw new RuntimeException("El cliente no tiene propiedades guardadas");
        }

        return favorites;
    }

    public ArrayList<UserInteraction> getUserInteractions(Client client) {
        if (client == null) {
            throw new RuntimeException("El cliente no puede ser nulo");
        }

        ArrayList<UserInteraction> interactions = client.getInteractionHistory();

        if (interactions == null || interactions.isEmpty()) {
            throw new RuntimeException("El cliente no tiene interacciones registradas");
        }

        return interactions;
    }
}
