package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

@Service
public class VisitService {

    VisitRepository visitRepository;

    public Visit registerVisit(Visit visit) {
        boolean exists = visitRepository.findById(visit.getId()).isPresent();

        if (exists) {
            throw new RuntimeException("A visit with this ID already exists");
        }

        visit.setId(CodeGenerator.generateVisitCode());
        return visitRepository.save(visit);
    }

    public Visit updateVisit(Visit visit) {
        if (visitRepository.findById(visit.getId()).isEmpty()) {
            throw new RuntimeException("No visit found with this ID");
        }

        return visitRepository.update(visit);
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