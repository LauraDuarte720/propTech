package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.repositories.OperationRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationService {

    OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation registerOperation(Operation operation) {
        if (operation == null) {
            throw new RuntimeException("La operación no puede ser nula");
        }

        if (operation.getOperationType() == null) {
            throw new RuntimeException("El tipo de operación no puede ser nulo");
        }

        operation.setId(CodeGenerator.generateOperationCode());
        return operationRepository.save(operation);
    }

    public Operation updateOperation(Operation operation) {
        return operationRepository.findById(operation.getId()).map(existing -> {
            Optional.ofNullable(operation.getProperty()).ifPresent(existing::setProperty);
            Optional.ofNullable(operation.getClient()).ifPresent(existing::setClient);
            Optional.ofNullable(operation.getAgent()).ifPresent(existing::setAgent);
            Optional.ofNullable(operation.getDateInitial()).ifPresent(existing::setDateInitial);
            Optional.ofNullable(operation.getDateFinal()).ifPresent(existing::setDateFinal);
            Optional.ofNullable(operation.getOperationType()).ifPresent(existing::setOperationType);
            Optional.ofNullable(operation.getValue()).ifPresent(existing::setValue);
            Optional.ofNullable(operation.getCommission()).ifPresent(existing::setCommission);
            Optional.ofNullable(operation.getProcessStatus()).ifPresent(existing::setProcessStatus);
            return operationRepository.update(existing);
        }).orElseThrow(() -> new RuntimeException("No existe una operación con ese ID: " + operation.getId()));
    }

    public void deleteOperation(Operation operation) {
        if (operationRepository.findById(operation.getId()).isEmpty()) {
            throw new RuntimeException("No existe una operación con ese ID");
        }

        operationRepository.deleteById(operation.getId());
    }

    public LinkedList<Operation> getAllOperations() {
        LinkedList<Operation> operations = operationRepository.getOperations();

        if (operations == null || operations.isEmpty()) {
            throw new RuntimeException("No hay operaciones registradas");
        }

        return operations;
    }

    public Operation getOperationById(String id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe una operación con ese ID: " + id));
    }

    public LinkedList<Operation> getOperationsByType(OperationType type) {
        if (type == null) {
            throw new RuntimeException("El tipo de operación no puede ser nulo");
        }

        LinkedList<Operation> all = getAllOperations();
        LinkedList<Operation> result = new LinkedList<>();

        for (int i = 0; i < all.size(); i++) {
            Operation op = all.get(i);
            if (op.getOperationType() == type) {
                result.addLast(op);
            }
        }

        if (result.isEmpty()) {
            throw new RuntimeException("No hay operaciones de tipo: " + type);
        }

        return result;
    }

    public LinkedList<Operation> getOperationsByAgent(Agent agent){

    }
}