package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "agent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent extends User{
    private String contact;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private GeographicZone assignedZone;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Property> assignedProperties;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Visit> scheduledVisits;
    private int closedDeals;
}