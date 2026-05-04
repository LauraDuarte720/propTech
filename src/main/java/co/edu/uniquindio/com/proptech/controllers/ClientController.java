package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoReturn;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
