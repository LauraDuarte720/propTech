package co.edu.uniquindio.com.proptech.domain.model;


import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
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

    private HashTable<String, Client> clients;

    private HashTable<String, Agent> agents;

    private List<Operation> operations;
    
    private List<Visit> visits;

    private List<Alert> alerts;
}
