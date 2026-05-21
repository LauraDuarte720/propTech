package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.InvalidVisitTransitionException;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.VisitAlreadyExists;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.VisitDoesNotExist;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.VisitSchedulingConflictException;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
        validateNoSchedulingConflict(visit);
        visit.setId(CodeGenerator.generateVisitCode());
        visit.setStatus(VisitStatus.PENDING);
        visit.setCreatedAt(LocalDateTime.now());
        return visitRepository.save(visit);
    }

    private void validateNoSchedulingConflict(Visit visit) {
        LinkedList<Visit> agentVisits = visitRepository.getVisitsByAgent(visit.getAgent().getCedula());
        for (Visit v : agentVisits) {
            if (v.getStatus() != VisitStatus.CANCELED
                    && v.getStatus() != VisitStatus.COMPLETED) {
                long diff = Math.abs(Duration.between(v.getDate(), visit.getDate()).toMinutes());
                if (diff < 60) {
                    throw new VisitSchedulingConflictException(visit.getAgent().getCedula(), visit.getDate());
                }
            }
        }
    }

    public Visit updateVisit(Visit visit) {
        return visitRepository.findById(visit.getId()).map(existing -> {
            Optional.ofNullable(visit.getDate()).ifPresent(existing::setDate);
            Optional.ofNullable(visit.getClient()).ifPresent(existing::setClient);
            Optional.ofNullable(visit.getProperty()).ifPresent(existing::setProperty);
            Optional.ofNullable(visit.getStatus()).ifPresent(newStatus -> {
                validateTransition(existing.getStatus(), newStatus);
                existing.setStatus(newStatus);
            });
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

    public HashTable<String, Integer> getFrequencyByProperty() {
        return visitRepository.getVisitFrequencyByProperty();
    }

    public HashTable<City, Integer> getFrequencyByCity() {
        return visitRepository.getVisitFrequencyByCity();
    }

    public HashTable<Zone, Integer> getFrequencyByZone(City city) {
        HashTable<Zone, Integer> result = visitRepository.getVisitFrequencyByCityZone().get(city);
        return result == null ? new HashTable<>() : result;
    }

    public HashTable<String, Integer> getFrequencyByNeighborhood(City city, Zone zone) {
        HashTable<Zone, HashTable<String, Integer>> byZone = visitRepository.getVisitsFrequenciesByCityZoneNeighbor().get(city);
        if (byZone == null) return new HashTable<>();
        HashTable<String, Integer> byNeighborhood = byZone.get(zone);
        return byNeighborhood == null ? new HashTable<>() : byNeighborhood;
    }

    public Visit updateVisitStatus(String visitId, VisitStatus newStatus) {
        Visit visit = getVisitById(visitId);
        validateTransition(visit.getStatus(), newStatus);
        visit.setStatus(newStatus);
        return visit;
    }

    private void validateTransition(VisitStatus current, VisitStatus next) {
        if (current == VisitStatus.COMPLETED || current == VisitStatus.CANCELED) {
            throw new InvalidVisitTransitionException(current, next, "Terminal state, cannot be modified");
        }
        if (next == VisitStatus.PENDING) {
            throw new InvalidVisitTransitionException(current, next, "Cannot return to pending");
        }
    }
}