package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;

import java.util.Optional;

public interface BasicAlertRepository {
    BasicAlert save(BasicAlert alert);
    Optional<BasicAlert> findById(String id);
    boolean deleteById(String id);
    BasicAlert update(BasicAlert alert);
}