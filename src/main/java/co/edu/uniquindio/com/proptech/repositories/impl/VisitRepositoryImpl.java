package co.edu.uniquindio.com.proptech.repository.impl;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import java.util.Optional;

public class VisitRepositoryImpl implements VisitRepository {

    private final PropTech propTech;

    public VisitRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Visit save(Visit visit) {
        return propTech.addVisit(visit);
    }

    @Override
    public Optional<Visit> findById(String id) {
        return Optional.ofNullable(propTech.getVisit(id));
    }

    @Override
    public boolean deleteById(String id) {
        return propTech.removeVisit(id);
    }
}