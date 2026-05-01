package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Alert;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AlertRepository;
import java.util.Optional;

public class AlertRepositoryImpl implements AlertRepository {

    private final PropTech propTech;

    public AlertRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Alert save(Alert alert) {
        return propTech.addAlert(alert);
    }

    @Override
    public Optional<Alert> findById(String id) {
        return Optional.ofNullable(propTech.getAlert(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeAlert(id);
    }
}