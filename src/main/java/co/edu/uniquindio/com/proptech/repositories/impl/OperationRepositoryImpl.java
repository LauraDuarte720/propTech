package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.OperationType;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.OperationRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class OperationRepositoryImpl implements OperationRepository {

    private final PropTech propTech;

    public OperationRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Operation save(Operation operation) {
        propTech.getOperations().addLast(operation);
        return operation;
    }

    @Override
    public Optional<Operation> findById(String id) {
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            if (propTech.getOperations().get(i).getId().equals(id))
                return Optional.of(propTech.getOperations().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            if (propTech.getOperations().get(i).getId().equals(id)) {
                propTech.getOperations().removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Operation update(Operation operation) {
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            if (propTech.getOperations().get(i).getId().equals(operation.getId())) {
                propTech.getOperations().set(i, operation);
            }
        }
        return operation;
    }

    @Override
    public LinkedList<Operation> getOperations() {
        return propTech.getOperations();
    }

    @Override
    public LinkedList<Operation> getOperationsByType(OperationType operationType) {
        LinkedList<Operation> result = new LinkedList<>();
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            Operation op = propTech.getOperations().get(i);
            if (op.getOperationType() == operationType)
                result.addLast(op);
        }
        return result;
    }

    @Override
    public LinkedList<Operation> getOperationsByAgent(Agent agent) {
        LinkedList<Operation> result = new LinkedList<>();
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            Operation op = propTech.getOperations().get(i);
            if (op.getAgent().equals(agent))
                result.addLast(op);
        }
        return result;
    }

    @Override
    public LinkedList<Operation> getOperationsByProperty(String propertyCode) {
        LinkedList<Operation> result = new LinkedList<>();
        for (int i = 0; i < propTech.getOperations().size(); i++) {
            Operation op = propTech.getOperations().get(i);
            if (op.getProperty() != null && op.getProperty().getCode().equals(propertyCode))
                result.addLast(op);
        }
        return result;
    }
}