package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.AbnormalAlert;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AbnormalAlertRepository;

import java.util.Optional;

public class AbnormalAlertRepositoryImpl implements AbnormalAlertRepository {

    private final PropTech propTech;

    public AbnormalAlertRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public AbnormalAlert save(AbnormalAlert alert) {
        return propTech.addAbnormalAlert(alert);
    }

    @Override
    public Optional<AbnormalAlert> findById(String id) {
        return Optional.ofNullable(propTech.getAbnormalAlert(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeAbnormalAlert(id);
    }

    @Override
    public AbnormalAlert update(AbnormalAlert alert) {
        return propTech.updateAbnormalAlert(alert);
    }
}