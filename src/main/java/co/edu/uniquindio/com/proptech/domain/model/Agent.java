package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agent")

public class Agent extends User{
    @Column(nullable = false, length = 10)
    private String contact;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private GeographicZone assignedZone;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Property> assignedProperties;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Visit> scheduledVisits;

    @Column(nullable = false)
    private int closedDeals;
}