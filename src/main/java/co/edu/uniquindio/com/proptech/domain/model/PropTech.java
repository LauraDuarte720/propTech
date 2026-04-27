package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "proptech")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropTech {
    @Id
    private String NIT;
    private List<Property> properties;
    private List<Client> clients;
    private List<Agent> agents;
    private List<Operation> operations;
    private List<Visit> visits;
    private List<Alert> alerts;
}
