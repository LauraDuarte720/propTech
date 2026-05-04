package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;

import java.util.Optional;

public interface OperationRepository {
    Operation save(Operation operation);
    Optional<Operation> findById(String id);
    boolean deleteById(String id);
    Operation update(Operation operation);
    LinkedList<Operation> getOperations();
    LinkedList<Operation> getOperationsByType();
    LinkedList<Operation> getOperationsByAgent();
}