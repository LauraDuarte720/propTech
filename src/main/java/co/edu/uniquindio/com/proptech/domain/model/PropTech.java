package co.edu.uniquindio.com.proptech.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropTech {
    
    private String NIT;
    
    private List<Property> properties;

    private List<Client> clients;

    private List<Agent> agents;

    private List<Operation> operations;
    
    private List<Visit> visits;

    private List<Alert> alerts;
}
