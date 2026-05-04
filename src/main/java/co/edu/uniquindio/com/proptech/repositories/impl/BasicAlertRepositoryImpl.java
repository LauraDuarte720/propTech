package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.BasicAlertRepository;
import java.util.Optional;

public class BasicAlertRepositoryImpl implements BasicAlertRepository {

    private final PropTech propTech;

    public BasicAlertRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public BasicAlert save(BasicAlert alert) {
        return propTech.addBasicAlert(alert);
    }

    @Override
    public Optional<BasicAlert> findById(String id) {
        return Optional.ofNullable(propTech.getBasicAlert(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeBasicAlert(id);
    }

    @Override
    public BasicAlert update(BasicAlert alert) {
        return propTech.updateBasicAlert(alert);
    }
}