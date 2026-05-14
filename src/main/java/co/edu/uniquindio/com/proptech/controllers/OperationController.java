package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.OperationDtoReturn;
import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.services.OperationService;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper;

    public OperationController(OperationService operationService, MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper) {
        this.operationService = operationService;
        this.operationMapper = operationMapper;
    }

    @PostMapping
    public ResponseEntity<OperationDtoReturn> createOperation(@Validated @RequestBody OperationDtoCreate operationDtoCreate) {
        Operation operation = operationMapper.toEntity(operationDtoCreate);
        Operation saved = operationService.registerOperation(operation);
        return ResponseEntity.ok(operationMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDtoReturn> getOperation(@PathVariable String id) {
        Operation operation = operationService.getOperationById(id);
        return ResponseEntity.ok(operationMapper.toDto(operation));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OperationDtoReturn> updateOperation(@PathVariable String id, @Validated @RequestBody OperationDtoUpdate operationDtoUpdate) {
        Operation operation = operationMapper.toUpdate(operationDtoUpdate);
        operation.setId(id);
        Operation updated = operationService.updateOperation(operation);
        return ResponseEntity.ok(operationMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable String id) {
        operationService.deleteOperation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OperationDtoReturn>> getOperations() {
        LinkedList<Operation> operations = operationService.getAllOperations();
        List<OperationDtoReturn> result = new java.util.ArrayList<>();
        for (int i = 0; i < operations.size(); i++) {
            result.add(operationMapper.toDto(operations.get(i)));
        }
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/type/{type}")
//    public ResponseEntity<List<OperationDtoReturn>> getOperationsByType(@PathVariable OperationType type) {
//        LinkedList<Operation> operations = operationService.getOperationsByType(type);
//        List<OperationDtoReturn> result = new java.util.ArrayList<>();
//        for (int i = 0; i < operations.size(); i++) {
//            result.add(operationMapper.toDto(operations.get(i)));
//        }
//        return ResponseEntity.ok(result);
//    }

}