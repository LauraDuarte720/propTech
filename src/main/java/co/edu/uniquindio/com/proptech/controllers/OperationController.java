package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.OperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper;
    private final StructuresMappers structuresMappers;

    public OperationController(OperationService operationService,
                               MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper,
                               StructuresMappers structuresMappers) {
        this.operationService = operationService;
        this.operationMapper = operationMapper;
        this.structuresMappers = structuresMappers;
    }

    // CRUD BÁSICO

    @PostMapping
    public ResponseEntity<OperationDtoReturn> createOperation(
            @Validated @RequestBody OperationDtoCreate dto) {
        Operation saved = operationService.registerOperation(operationMapper.toEntity(dto));
        return ResponseEntity.ok(operationMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDtoReturn> getOperation(@PathVariable String id) {
        return ResponseEntity.ok(operationMapper.toDto(operationService.getOperationById(id)));
    }

    @GetMapping
    public ResponseEntity<List<OperationDtoReturn>> getAllOperations() {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        operationService.getAllOperations(),
                        operationMapper::toDto
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OperationDtoReturn> updateOperation(
            @PathVariable String id,
            @Validated @RequestBody OperationDtoUpdate dto) {
        Operation operation = operationMapper.toUpdate(dto);
        operation.setId(id);
        return ResponseEntity.ok(operationMapper.toDto(operationService.updateOperation(operation)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable String id) {
        operationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }

    // FILTROS

    @GetMapping("/type/{type}")
    public ResponseEntity<List<OperationDtoReturn>> getOperationsByType(
            @PathVariable OperationType type) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        operationService.getOperationsByType(type),
                        operationMapper::toDto
                )
        );
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<OperationDtoReturn>> getOperationsByAgent(
            @PathVariable String agentId) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        operationService.getOperationsByAgent(agentId),
                        operationMapper::toDto
                )
        );
    }

    @GetMapping("/property/{propertyCode}")
    public ResponseEntity<List<OperationDtoReturn>> getOperationsByProperty(
            @PathVariable String propertyCode) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        operationService.getOperationsByProperty(propertyCode),
                        operationMapper::toDto
                )
        );
    }
}