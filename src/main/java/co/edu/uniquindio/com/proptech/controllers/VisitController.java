package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.VisitService;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;
    private final MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper;
    private final StructuresMappers structuresMappers;

    public VisitController(VisitService visitService,
                           MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper,
                           StructuresMappers structuresMappers) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
        this.structuresMappers = structuresMappers;
    }

    // ══════════════════════════════════════════════
    // CRUD BÁSICO
    // ══════════════════════════════════════════════

    @PostMapping
    public ResponseEntity<VisitDtoReturn> createVisit(
            @Validated @RequestBody VisitDtoCreate dto) {
        return ResponseEntity.ok(
                visitMapper.toDto(visitService.registerVisit(visitMapper.toEntity(dto)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDtoReturn> getVisit(@PathVariable String id) {
        return ResponseEntity.ok(visitMapper.toDto(visitService.getVisitById(id)));
    }

    @GetMapping
    public ResponseEntity<List<VisitDtoReturn>> getAllVisits() {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        visitService.getAllVisits(),
                        visitMapper::toDto
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VisitDtoReturn> updateVisit(
            @PathVariable String id,
            @Validated @RequestBody VisitDtoUpdate dto) {
        Visit visit = visitMapper.toUpdate(dto);
        visit.setId(id);
        return ResponseEntity.ok(visitMapper.toDto(visitService.updateVisit(visit)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VisitDtoReturn> updateVisitStatus(
            @PathVariable String id,
            @RequestParam VisitStatus status) {
        return ResponseEntity.ok(
                visitMapper.toDto(visitService.updateVisitStatus(id, status))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable String id) {
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }

    // ══════════════════════════════════════════════
    // FILTROS
    // ══════════════════════════════════════════════

    @GetMapping("/property/{propertyCode}")
    public ResponseEntity<List<VisitDtoReturn>> getVisitsByProperty(
            @PathVariable String propertyCode) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        visitService.getVisitsByProperty(propertyCode),
                        visitMapper::toDto
                )
        );
    }

    @GetMapping("/client/{clientCedula}")
    public ResponseEntity<List<VisitDtoReturn>> getVisitsByClient(
            @PathVariable String clientCedula) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        visitService.getVisitsByClient(clientCedula),
                        visitMapper::toDto
                )
        );
    }

    @GetMapping("/agent/{agentCedula}")
    public ResponseEntity<List<VisitDtoReturn>> getVisitsByAgent(
            @PathVariable String agentCedula) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        visitService.getVisitsByAgent(agentCedula),
                        visitMapper::toDto
                )
        );
    }

    // ══════════════════════════════════════════════
    // FRECUENCIAS
    // ══════════════════════════════════════════════

    @GetMapping("/frequency/property")
    public ResponseEntity<java.util.Map<String, Integer>> getFrequencyByProperty() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToMap(visitService.getFrequencyByProperty())
        );
    }

    @GetMapping("/frequency/city")
    public ResponseEntity<java.util.Map<String, Integer>> getFrequencyByCity() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToMap(visitService.getFrequencyByCity())
        );
    }

    @GetMapping("/frequency/zone")
    public ResponseEntity<java.util.Map<String, Integer>> getFrequencyByZone(
            @RequestParam City city) {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToMap(visitService.getFrequencyByZone(city))
        );
    }

    @GetMapping("/frequency/neighborhood")
    public ResponseEntity<java.util.Map<String, Integer>> getFrequencyByNeighborhood(
            @RequestParam City city,
            @RequestParam Zone zone) {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToMap(visitService.getFrequencyByNeighborhood(city, zone))
        );
    }
}