package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.VisitStatus;
import co.edu.uniquindio.com.proptech.domain.enums.VisitType;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.*;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.utils.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VisitService {

    VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit registerVisit(Agent agent, Visit visit) {
        boolean exists = visitRepository.findById(visit.getId()).isPresent();
        if (exists) {
            throw new VisitAlreadyExists("id", visit.getId());
        }
        LocalDateTime date = visit.getDate();
        validateNoSchedulingConflict(agent, visit, date);
        visit.setId(CodeGenerator.generateVisitCode());
        visit.setCreatedAt(LocalDateTime.now());
            visit.setStatus(VisitStatus.PENDING);
        return visitRepository.save(visit);
    }


    private void validateNoSchedulingConflict(Agent agent, Visit visit, LocalDateTime newDate) {
        PriorityQueue<Visit> agentVisits = agent.getScheduledVisits();
        for (Visit v : agentVisits) {
            if (v.getStatus() == VisitStatus.CANCELED ||
                    v.getStatus() == VisitStatus.COMPLETED || v.equals(visit)) continue;
            long diff = Math.abs(Duration.between(v.getDate(), visit.getDate()).toMinutes());
            if (diff < 60) {
                if (visit.getVisitType() == VisitType.VIP) {
                    v.setStatus(VisitStatus.PENDINGRESCHEDULE);
                    throw new VipVisitDisplacementException(
                            visit.getAgent().getCedula(),
                            v.getId()
                    );
                }
                throw new VisitSchedulingConflictException(
                        visit.getAgent().getCedula(),
                        visit.getDate()
                );
            }
        }
    }


    public Visit updateVisit(Visit visit) {
        return visitRepository.findById(visit.getId()).map(existing -> {

            boolean onlyUpdatingNotes =
                    visit.getPostVisitNotes() != null &&
                            visit.getClient() == null &&
                            visit.getProperty() == null &&
                            visit.getVisitType() == null &&
                            visit.getStatus() == null;

            if (onlyUpdatingNotes) {

                if (existing.getStatus() != VisitStatus.COMPLETED) {
                    throw new InvalidVisitUpdate(
                            existing.getStatus(),
                            "Post visit notes can only be added to completed visits"
                    );
                }

                existing.setPostVisitNotes(visit.getPostVisitNotes());
                return visitRepository.update(existing);
            }
            if (existing.getStatus() == VisitStatus.COMPLETED ||
                    existing.getStatus() == VisitStatus.CANCELED) {
                throw new InvalidVisitUpdate(
                        existing.getStatus(),
                        "Completed or canceled visits cannot be modified"
                );
            }
            Optional.ofNullable(visit.getClient()).ifPresent(existing::setClient);
            Optional.ofNullable(visit.getProperty()).ifPresent(existing::setProperty);
            Optional.ofNullable(visit.getVisitType()).ifPresent(existing::setVisitType);
            Optional.ofNullable(visit.getStatus()).ifPresent(newStatus -> {
                validateTransition(existing.getStatus(), newStatus);
                existing.setStatus(newStatus);
            });

            return visitRepository.update(existing);

        }).orElseThrow(() -> new VisitDoesNotExist("id", visit.getId()));
    }

    public Visit rescheduleVisit(Visit visit, LocalDateTime newDate) {
        Agent agent = visit.getAgent();
        validateTransition(visit.getStatus(), VisitStatus.RESCHEDULED);
        agent.removeVisitFromQueue(visit);
        visit.setDate(newDate);
        visit.setStatus(VisitStatus.RESCHEDULED);

        agent.enqueueVisit(visit);

        return visit;
    }

    public Visit cancelVisit(Visit visit) {
        validateTransition(visit.getStatus(), VisitStatus.CANCELED);
        visit.setStatus(VisitStatus.CANCELED);
        return visit;
    }

    public Visit confirmVisit(Visit visit) {
        validateTransition(visit.getStatus(), VisitStatus.CONFIRMED);
        visit.setStatus(VisitStatus.CONFIRMED);
        return visit;
    }

    public void deleteVisit(String visitId) {
        if (visitRepository.findById(visitId).isEmpty()) {
            throw new VisitDoesNotExist("id", visitId);
        }

        visitRepository.deleteById(visitId);
    }

    public void updateExpiredVisits() {
        LocalDateTime now = LocalDateTime.now();
        for (Visit visit : visitRepository.getAllVisits()) {
            if (visit.getStatus() == VisitStatus.COMPLETED ||
                    visit.getStatus() == VisitStatus.CANCELED ||
                    visit.getStatus() == VisitStatus.EXPIRED) {
                continue;
            }
            if (visit.getDate() != null &&
                    visit.getDate().plusDays(1).isBefore(now)) {
                visit.setStatus(VisitStatus.EXPIRED);
            }
        }
    }

    public LinkedList<Visit> getAllVisits() {
        updateExpiredVisits();
        return visitRepository.getAllVisits();
    }

    public LinkedList<Visit> getAllAgentVisitHistory(String agentCedula) {
        updateExpiredVisits();
        return visitRepository.getVisitsByAgent(agentCedula);
    }

    public Visit getVisitById(String id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new VisitDoesNotExist("id", id));
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

    public void validateTransition(VisitStatus current, VisitStatus next) {
        if (current == VisitStatus.COMPLETED || current == VisitStatus.CANCELED) {
            throw new InvalidVisitTransitionException(current, next, "Terminal state, cannot be modified");
        }
        if (next == VisitStatus.PENDING) {
            throw new InvalidVisitTransitionException(current, next, "Cannot return to pending");
        }
        if (
                current == VisitStatus.EXPIRED && next != VisitStatus.CANCELED && next != VisitStatus.RESCHEDULED
        ){throw new InvalidVisitTransitionException(current, next, "Can only cancel or reprogram");}
        if(next == VisitStatus.COMPLETED && current != VisitStatus.CONFIRMED){
            throw new InvalidVisitTransitionException(current, next, "Cannot complete unconfirmed visit");
        }
        if(next == VisitStatus.CONFIRMED && current != VisitStatus.PENDING && current != VisitStatus.RESCHEDULED){
            throw new InvalidVisitTransitionException(current, next, "Cannot confirm visit if it is not pending or rescheduled");
        }

    }

    public boolean hasVisitsForPropertyAfter(String propertyCode, LocalDateTime after) {
        for (Visit visit : visitRepository.getVisitsByProperty(propertyCode)) {
            if (visit.getCreatedAt() != null && visit.getCreatedAt().isAfter(after)) {
                return true;
            }
        }
        return false;
    }
}