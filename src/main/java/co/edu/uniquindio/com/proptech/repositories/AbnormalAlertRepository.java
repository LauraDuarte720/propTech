package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.AbnormalAlert;

import java.util.Optional;

public interface AbnormalAlertRepository {
    AbnormalAlert save(AbnormalAlert alert);
    Optional<AbnormalAlert> findById(String id);
    boolean deleteById(String id);
    AbnormalAlert update(AbnormalAlert alert);
}