package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAbnormalType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import co.edu.uniquindio.com.proptech.domain.model.AbnormalAlert;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;

import java.util.Optional;

public interface AbnormalAlertRepository {
    AbnormalAlert save(AbnormalAlert alert);
    Optional<AbnormalAlert> findById(String id);
    boolean deleteById(String id);
    AbnormalAlert update(AbnormalAlert alert);
    ArrayList<AbnormalAlert> getAll();
    ArrayList<AbnormalAlert> getByType(AlertAbnormalType type);
    ArrayList<AbnormalAlert> getByLevel(AttentionLevel level);
}