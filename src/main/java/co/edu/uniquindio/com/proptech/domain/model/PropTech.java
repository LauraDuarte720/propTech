package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "proptech")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropTech {
    @Id
    private String NIT;


    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "proptech")
    private List<Property> properties;

    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Client> clients;

    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Agent> agents;

    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Operation> operations;

    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Visit> visits;

    @JoinColumn(name = "proptech_id")
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Alert> alerts;
}
