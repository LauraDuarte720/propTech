package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.VisitAlreadyExists;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.VisitDoesNotExist;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VisitService {

    VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit registerVisit(Visit visit) {
        boolean exists = visitRepository.findById(visit.getId()).isPresent();

        if (exists) {
            throw new VisitAlreadyExists("id", visit.getId());
        }

        visit.setId(CodeGenerator.generateVisitCode());
        visit.setCreatedAt(LocalDateTime.now());
        return visitRepository.save(visit);
    }

    public Visit updateVisit(Visit visit) {
        return visitRepository.findById(visit.getId()).map(existing -> {
            Optional.ofNullable(visit.getDate()).ifPresent(existing::setDate);
            Optional.ofNullable(visit.getClient()).ifPresent(existing::setClient);
            Optional.ofNullable(visit.getProperty()).ifPresent(existing::setProperty);
            Optional.ofNullable(visit.getStatus()).ifPresent(existing::setStatus);
            Optional.ofNullable(visit.getPostVisitNotes()).ifPresent(existing::setPostVisitNotes);
            return visitRepository.update(existing);
        }).orElseThrow(() -> new VisitDoesNotExist("id", visit.getId()));
    }

    public void deleteVisit(String visitId) {
        if (visitRepository.findById(visitId).isEmpty()) {
            throw new VisitDoesNotExist("id", visitId);
        }

        visitRepository.deleteById(visitId);
    }

    public LinkedList<Visit> getAllVisits() {
        return visitRepository.getAllVisits();
    }

    public Visit getVisitById(String id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No visit found with this ID: " + id));
    }

    public LinkedList<Visit> getVisitsByProperty(String propertyCode) {
        return visitRepository.getVisitsByProperty(propertyCode);
    }

    public LinkedList<Visit> getVisitsByClient(String clientCedula) {
        return visitRepository.getVisitsByClient(clientCedula);
    }

    public LinkedList<Visit> getVisitsByAgent(String agentCedula) {
        return visitRepository.getVisitsByAgent(agentCedula);
    }
}