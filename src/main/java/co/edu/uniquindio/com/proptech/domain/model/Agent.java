package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.priorityQueue.PriorityQueue;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Agent extends User{
    private String contact;

    private GeographicZone assignedZone;

    private ArrayList<Property> assignedProperties;

    private PriorityQueue<Visit> scheduledVisits;

    private Queue<SupportRequest> supportRequests;

    private Integer closedDeals;

    public Agent(String cedula, String name,  String username, String password, String contact, GeographicZone assignedZone, Integer closedDeals) {
        super(cedula, name, username, password);
        this.contact = contact;
        this.assignedZone = assignedZone;
        this.assignedProperties = new ArrayList<>();
        this.scheduledVisits = new PriorityQueue<>(Comparator.comparing(Visit::getDate));
        this.supportRequests = new Queue<>();
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
}