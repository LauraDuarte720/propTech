package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Alert;
import java.util.Optional;

public interface AlertRepository {
    Alert save(Alert alert);
    Optional<Alert> findById(String id);
    boolean deleteById(String id);
    Alert update(Alert alert);
}