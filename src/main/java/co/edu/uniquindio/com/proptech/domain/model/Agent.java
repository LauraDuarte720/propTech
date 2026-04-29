package co.edu.uniquindio.com.proptech.domain.model;

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

    private List<Property> assignedProperties;

    private List<Visit> scheduledVisits;

    private int closedDeals;
}