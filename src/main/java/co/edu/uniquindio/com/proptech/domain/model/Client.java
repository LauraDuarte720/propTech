package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.ClientType;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.SearchStatus;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "client")
public class Client extends User {
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private double budget;
    @Column(nullable = false)
    private int minBedrooms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType clientType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SearchStatus searchStatus;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_interest_zones",
            joinColumns = @JoinColumn(name = "cedula"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<GeographicZone> interestZones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType desiredPropertyType;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<UserInteraction> interactionHistory;
}
