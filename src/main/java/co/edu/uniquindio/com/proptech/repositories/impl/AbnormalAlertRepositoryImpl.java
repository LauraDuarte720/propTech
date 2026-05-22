package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.AlertAbnormalType;
import co.edu.uniquindio.com.proptech.domain.enums.AttentionLevel;
import co.edu.uniquindio.com.proptech.domain.model.AbnormalAlert;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AbnormalAlertRepository;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class AbnormalAlertRepositoryImpl implements AbnormalAlertRepository {

    private final PropTech propTech;

    public AbnormalAlertRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public AbnormalAlert save(AbnormalAlert alert) {
        propTech.getAbnormalAlerts().add(alert);
        return alert;
    }

    @Override
    public Optional<AbnormalAlert> findById(String id) {
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            if (propTech.getAbnormalAlerts().get(i).getId().equals(id))
                return Optional.of(propTech.getAbnormalAlerts().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            if (propTech.getAbnormalAlerts().get(i).getId().equals(id)) {
                propTech.getAbnormalAlerts().remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public AbnormalAlert update(AbnormalAlert alert) {
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            if (propTech.getAbnormalAlerts().get(i).getId().equals(alert.getId())) {
                propTech.getAbnormalAlerts().set(i, alert);
            }
        }
        return alert;
    }

    @Override
    public ArrayList<AbnormalAlert> getAll() {
        ArrayList<AbnormalAlert> result = new ArrayList<>();
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            result.add(propTech.getAbnormalAlerts().get(i));
        }
        return result;
    }

    @Override
    public ArrayList<AbnormalAlert> getByType(AlertAbnormalType type) {
        ArrayList<AbnormalAlert> result = new ArrayList<>();
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            AbnormalAlert alert = propTech.getAbnormalAlerts().get(i);
            if (alert.getAlertAbnormalType() == type) result.add(alert);
        }
        return result;
    }

    @Override
    public ArrayList<AbnormalAlert> getByLevel(AttentionLevel level) {
        ArrayList<AbnormalAlert> result = new ArrayList<>();
        for (int i = 0; i < propTech.getAbnormalAlerts().size(); i++) {
            AbnormalAlert alert = propTech.getAbnormalAlerts().get(i);
            if (alert.getAttentionLevel() == level) result.add(alert);
        }
        return result;
    }
}