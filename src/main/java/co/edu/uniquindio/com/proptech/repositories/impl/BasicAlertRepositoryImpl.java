package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.BasicAlertRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class BasicAlertRepositoryImpl implements BasicAlertRepository {

    private final PropTech propTech;

    public BasicAlertRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public BasicAlert save(BasicAlert alert) {
        propTech.getBasicAlerts().add(alert);
        return alert;
    }

    @Override
    public Optional<BasicAlert> findById(String id) {
        for (int i = 0; i < propTech.getBasicAlerts().size(); i++) {
            if (propTech.getBasicAlerts().get(i).getId().equals(id))
                return Optional.of(propTech.getBasicAlerts().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getBasicAlerts().size(); i++) {
            if (propTech.getBasicAlerts().get(i).getId().equals(id)) {
                propTech.getBasicAlerts().remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public BasicAlert update(BasicAlert alert) {
        for (int i = 0; i < propTech.getBasicAlerts().size(); i++) {
            if (propTech.getBasicAlerts().get(i).getId().equals(alert.getId())) {
                propTech.getBasicAlerts().set(i, alert);
            }
        }
        return alert;
    }
}