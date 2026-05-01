package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

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

}
