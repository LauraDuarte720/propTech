package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.Optional;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Agent extends User implements Comparable<Agent> {
    private String contact;

    private GeographicZone assignedZone;

    @Builder.Default
    private ArrayList<Property> assignedProperties = new ArrayList<>();

    @Builder.Default
    private PriorityQueue<Visit> scheduledVisits = new PriorityQueue<>(Comparator.comparing(Visit::getDate));

    @Builder.Default
    private Queue<SupportRequest> supportRequests = new Queue<>();

    @Builder.Default
    private LinkedList<SupportRequest> supportHistory = new LinkedList<>();

    @Builder.Default
    private Integer closedDeals = 0;

    public Agent(String cedula, String name,  String username, String password, String contact, GeographicZone assignedZone, Integer closedDeals) {
        super(cedula, name, username, password);
        this.contact = contact;
        this.assignedZone = assignedZone;
        this.closedDeals = closedDeals;

    }


    public Property addProperty(Property property) {
        return assignedProperties.add(property);
    }

    public Property getProperty(String code) {
        for (int i = 0; i < assignedProperties.size(); i++) {
            Property p = assignedProperties.get(i);
            if (p.getCode().equals(code)) return p;
        }
        return null;
    }

    public void updateProperty(Property property) {
        for (int i = 0; i < assignedProperties.size(); i++) {
            if (assignedProperties.get(i).getCode().equals(property.getCode())) {
                assignedProperties.set(i, property);
                return;
            }
        }
    }

    public boolean removeProperty(Property property) {
        return assignedProperties.remove(property);
    }

    public void enqueueVisit(Visit visit) {
        scheduledVisits.add(visit);
    }

    public Visit dequeueVisit() {
        return scheduledVisits.poll();
    }

    public Visit peekNextVisit() {
        return scheduledVisits.peek();
    }

    public boolean hasVisits() {
        return !scheduledVisits.isEmpty();
    }

    public void increaseClosedDeals() {
        closedDeals++;
    }

    public void enqueueSupportRequest(SupportRequest request) {
        supportRequests.enqueue(request);
    }
    public SupportRequest dequeueSupportRequest() {
        return supportRequests.dequeue();
    }
    public SupportRequest peekNextSupportRequest() {
        return supportRequests.peekFront();
    }
    public boolean hasSupportRequests() {
        return !supportRequests.isEmpty();
    }

    public Optional<SupportRequest> findSupportRequest(String requestId) {
        for (SupportRequest sr : supportRequests) {
            if (sr.getId().equals(requestId)) return Optional.of(sr);
        }
        for (SupportRequest sr : supportHistory) {
            if (sr.getId().equals(requestId)) return Optional.of(sr);
        }
        return Optional.empty();
    }

    public void addToSupportHistory(SupportRequest request) {
        supportHistory.addLast(request);
    }

    @Override
    public int compareTo(Agent o) {
        return Integer.compare(this.closedDeals, o.getClosedDeals());
    }
}