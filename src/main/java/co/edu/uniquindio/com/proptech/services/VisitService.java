package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
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
            throw new RuntimeException("A visit with this ID already exists");
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
        }).orElseThrow(() -> new RuntimeException("No visit found with this ID: " + visit.getId()));
    }

    public void deleteVisit(Visit visit) {
        if (visitRepository.findById(visit.getId()).isEmpty()) {
            throw new RuntimeException("No visit found with this ID");
        }

        visitRepository.deleteById(visit.getId());
    }

    public LinkedList<Visit> getAllVisits() {
        LinkedList<Visit> visits = visitRepository.getAllVisits();

        if (visits == null || visits.isEmpty()) {
            throw new RuntimeException("No visits registered");
        }

        return visits;
    }

    public Visit getVisitById(String id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No visit found with this ID: " + id));
    }
}