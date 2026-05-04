package co.edu.uniquindio.com.proptech.repositories.impl;

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
        return propTech.addOperation(operation);
    }

    @Override
    public Optional<Operation> findById(String id) {
        return Optional.ofNullable(propTech.getOperation(id));
    }


    @Override
    public boolean deleteById(String id) {
        return propTech.removeOperation(id);
    }

    @Override
    public Operation update(Operation operation) {
        return propTech.updateOperation(operation);
    }

    @Override
    public LinkedList<Operation> getOperations() {
        return propTech.getOperations();
    }

}