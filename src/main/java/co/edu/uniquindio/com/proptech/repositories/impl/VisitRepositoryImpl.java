package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class VisitRepositoryImpl implements VisitRepository {

    private final PropTech propTech;

    public VisitRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Visit save(Visit visit) {
        propTech.getVisits().addLast(visit);
        return visit;
    }

    @Override
    public Optional<Visit> findById(String id) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            if (propTech.getVisits().get(i).getId().equals(id))
                return Optional.of(propTech.getVisits().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            if (propTech.getVisits().get(i).getId().equals(id)) {
                propTech.getVisits().removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Visit update(Visit visit) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            if (propTech.getVisits().get(i).getId().equals(visit.getId())) {
                propTech.getVisits().set(i, visit);
            }
        }
        return visit;
    }

    @Override
    public LinkedList<Visit> getAllVisits() {
        return propTech.getVisits();
    }

    @Override
    public LinkedList<Visit> getVisitsByProperty(String propertyCode) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getProperty() != null && visit.getProperty().getCode().equals(propertyCode))
                result.addLast(visit);
        }
        return result;
    }

    @Override
    public LinkedList<Visit> getVisitsByClient(String clientCedula) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getClient() != null && visit.getClient().getCedula().equals(clientCedula))
                result.addLast(visit);
        }
        return result;
    }

    @Override
    public LinkedList<Visit> getVisitsByAgent(String agentCedula) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getAgent() != null && visit.getAgent().getCedula().equals(agentCedula))
                result.addLast(visit);
        }
        return result;
    }
}