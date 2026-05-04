package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoReturn;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private final MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;

    public ClientController(ClientService clientService, MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientDtoReturn> createClient(@Validated @RequestBody ClientDtoCreate clientDto){
        Client client = clientMapper.toEntity(clientDto);
        Client saved = clientService.registerClient(client);
        return ResponseEntity.ok(clientMapper.toDto(saved));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<ClientDtoReturn> getClient(@PathVariable String cedula){
        Client client = clientService.getClientByCedula(cedula);
        return ResponseEntity.ok(clientMapper.toDto(client));
    }

    @PatchMapping("/{cedula}")
    public ResponseEntity<ClientDtoReturn> updateClient(@PathVariable String cedula, @Validated @RequestBody ClientDtoUpdate clientDto){
        Client client = clientMapper.toUpdate(clientDto);
        client.setCedula(cedula);
        Client updated = clientService.updateClient(client);
        return ResponseEntity.ok(clientMapper.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<ClientDtoReturn>> getClients() {
        HashTable<String, Client> clients = clientService.getClients();
        List<ClientDtoReturn> result = new ArrayList<>();
        for (Client client : clients.values()) {
            result.add(clientMapper.toDto(client));
        }
        return ResponseEntity.ok(result);
    }
}
