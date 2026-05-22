package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.enums.InteractionType;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.mappers.MapperCreate;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;
    private final MapperCreate<UserInteraction, UserInteractionDtoCreate, UserInteractionDtoReturn> userInteractionMapper;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final StructuresMappers structuresMappers;

    public ClientController(ClientService clientService,
                            MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper,
                            MapperCreate<UserInteraction, UserInteractionDtoCreate, UserInteractionDtoReturn> userInteractionMapper,
                            MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper,
                            StructuresMappers structuresMappers) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
        this.userInteractionMapper = userInteractionMapper;
        this.propertyMapper = propertyMapper;
        this.structuresMappers = structuresMappers;
    }

    // CRUD BÁSICO

    @PostMapping
    public ResponseEntity<ClientDtoReturn> createClient(
            @Validated @RequestBody ClientDtoCreate dto) {
        Client saved = clientService.registerClient(clientMapper.toEntity(dto));
        return ResponseEntity.ok(clientMapper.toDto(saved));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<ClientDtoReturn> getClient(@PathVariable String cedula) {
        return ResponseEntity.ok(clientMapper.toDto(clientService.getClientByCedula(cedula)));
    }

    @GetMapping
    public ResponseEntity<List<ClientDtoReturn>> getAllClients() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableValues(
                        clientService.getClients(),
                        clientMapper::toDto
                )
        );
    }

    @PatchMapping("/{cedula}")
    public ResponseEntity<ClientDtoReturn> updateClient(
            @PathVariable String cedula,
            @Validated @RequestBody ClientDtoUpdate dto) {
        Client client = clientMapper.toUpdate(dto);
        client.setCedula(cedula);
        return ResponseEntity.ok(clientMapper.toDto(clientService.updateClient(client)));
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> deleteClient(@PathVariable String cedula) {
        clientService.deleteClient(cedula);
        return ResponseEntity.noContent().build();
    }

    // INTERACCIONES

    @PostMapping("/{cedula}/interactions")
    public ResponseEntity<UserInteractionDtoReturn> registerInteraction(
            @PathVariable String cedula,
            @Validated @RequestBody UserInteractionDtoCreate dto) {
        dto.setClientId(cedula);
        UserInteraction interaction = userInteractionMapper.toEntity(dto);
        return ResponseEntity.ok(
                userInteractionMapper.toDto(clientService.registerUserInteraction(interaction))
        );
    }

    @GetMapping("/{cedula}/interactions")
    public ResponseEntity<List<UserInteractionDtoReturn>> getInteractions(
            @PathVariable String cedula) {
        var interactions = clientService.getUserInteractions(cedula);
        List<UserInteractionDtoReturn> result = new java.util.ArrayList<>();
        for (InteractionType type : InteractionType.values()) {
            var list = interactions.get(type);
            if (list == null) continue;
            for (int i = 0; i < list.size(); i++) {
                result.add(userInteractionMapper.toDto(list.get(i)));
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cedula}/interactions/{type}")
    public ResponseEntity<List<UserInteractionDtoReturn>> getInteractionsByType(
            @PathVariable String cedula,
            @PathVariable InteractionType type) {
        Client client = clientService.getClientByCedula(cedula);
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        client.getInteractionsByType(type),
                        userInteractionMapper::toDto
                )
        );
    }

    // FAVORITOS

    @GetMapping("/{cedula}/favorites")
    public ResponseEntity<List<PropertyDtoReturn>> getFavorites(@PathVariable String cedula) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        clientService.getFavorites(cedula),
                        propertyMapper::toDto
                )
        );
    }

    // ORDENAMIENTO Y FILTROS

    @GetMapping("/ordered-by-budget")
    public ResponseEntity<List<ClientDtoReturn>> getClientsOrderedByBudget() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        clientService.getClientsOrderedByBudget(),
                        clientMapper::toDto
                )
        );
    }

    @GetMapping("/budget-range")
    public ResponseEntity<List<ClientDtoReturn>> getClientsByBudgetRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        clientService.getClientsByBudgetRange(min, max),
                        clientMapper::toDto
                )
        );
    }
}