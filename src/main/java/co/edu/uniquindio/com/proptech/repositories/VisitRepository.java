package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
import java.util.Optional;

public interface VisitRepository {
    Visit save(Visit visit);
    Optional<Visit> findById(String id);
    boolean deleteById(String id);
}