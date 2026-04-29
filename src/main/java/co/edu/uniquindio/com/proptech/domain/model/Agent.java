package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Agent extends User{
    private String contact;

    private GeographicZone assignedZone;

    private ArrayList<Property> assignedProperties;

    private Queue<Visit> scheduledVisits;

    private int closedDeals;
}