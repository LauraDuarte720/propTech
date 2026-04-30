package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Operation;
import java.util.Optional;

public interface OperationRepository {
    void save(Operation operation);
    Optional<Operation> findById(String id);
    boolean deleteById(String id);
}