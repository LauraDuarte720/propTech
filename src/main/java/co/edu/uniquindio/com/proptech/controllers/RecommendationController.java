package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.RecommendationService;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;
    private final StructuresMappers structuresMappers;

    public RecommendationController(RecommendationService recommendationService,
                                    MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper,
                                    MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper,
                                    StructuresMappers structuresMappers) {
        this.recommendationService = recommendationService;
        this.propertyMapper = propertyMapper;
        this.clientMapper = clientMapper;
        this.structuresMappers = structuresMappers;
    }

    // Requisito 8 — propiedades recomendadas para un cliente (scoring completo)
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PropertyDtoReturn>> recommendProperties(
            @PathVariable String clientId) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        recommendationService.recommendProperties(clientId),
                        propertyMapper::toDto
                )
        );
    }

    // Requisito 12 — propiedades relacionadas con un cliente (análisis estructural del grafo)
    @GetMapping("/client/{clientId}/related-properties")
    public ResponseEntity<List<PropertyDtoReturn>> getRelatedProperties(
            @PathVariable String clientId) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        recommendationService.getPropertiesRelatedToClient(clientId),
                        propertyMapper::toDto
                )
        );
    }

    // Propiedades similares a una propiedad dada\
    @GetMapping("/property/{propertyCode}/similar")
    public ResponseEntity<List<PropertyDtoReturn>> getSimilarProperties(
            @PathVariable String propertyCode) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        recommendationService.getSimilarProperties(propertyCode),
                        propertyMapper::toDto
                )
        );
    }


    // Clientes potenciales para una propiedad específica
    @GetMapping("/property/{propertyCode}/potential-clients")
    public ResponseEntity<List<ClientDtoReturn>> getPotentialClientsForProperty(
            @PathVariable String propertyCode) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        recommendationService.getPotentialClientsForProperty(propertyCode),
                        clientMapper::toDto
                )
        );
    }

    // Clientes potenciales para un grupo de propiedades (body: lista de códigos)
    @PostMapping("/properties/potential-clients")
    public ResponseEntity<List<ClientDtoReturn>> getPotentialClientsForProperties(
            @RequestBody List<String> propertyCodes) {
        ArrayList<String> codes = new ArrayList<>();
        propertyCodes.forEach(codes::add);
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        recommendationService.getPotentialClientsForProperties(codes),
                        clientMapper::toDto
                )
        );
    }

    // Clientes más potenciales del sistema completo
    @GetMapping("/clients/most-potential")
    public ResponseEntity<List<ClientDtoReturn>> getMostPotentialClients() {
        return ResponseEntity.ok(
                structuresMappers.fromPriorityQueue(
                        recommendationService.getMostPotentialClients(),
                        clientMapper::toDto
                )
        );
    }

    // Inmuebles con mayor potencial de venta (basado en interacciones del grafo)
    @GetMapping("/properties/most-potential")
    public ResponseEntity<List<PropertyDtoReturn>> getMostPotentialProperties() {
        return ResponseEntity.ok(
                structuresMappers.fromPriorityQueue(
                        recommendationService.getMostPotentialProperties(),
                        propertyMapper::toDto
                )
        );
    }

    // Clientes con mayor intención de cierre
    @GetMapping("/clients/closing-intention")
    public ResponseEntity<List<ClientDtoReturn>> getClientsByClosingIntention() {
        return ResponseEntity.ok(
                structuresMappers.fromPriorityQueue(
                        recommendationService.getClientsByClosingIntention(),
                        clientMapper::toDto
                )
        );
    }

    // Propiedades con mayor demanda
    @GetMapping("/properties/demand")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesByDemand() {
        return ResponseEntity.ok(
                structuresMappers.fromPriorityQueue(
                        recommendationService.getPropertiesByDemand(),
                        propertyMapper::toDto
                )
        );
    }
}