package co.edu.uniquindio.com.proptech.domain.model;


import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropTech {
    
    private String NIT;
    
    private HashTable<String, Property> properties;

    private HashTable<String, Client> clients;

    private HashTable<String, Agent> agents;

    private LinkedList<Operation> operations;
    
    private LinkedList<Visit> visits;

    private ArrayList<Alert> alerts;

    public PropTech(String NIT) {
        this.NIT = NIT;
        this.properties = new HashTable<>();
        this.clients = new HashTable<>();
        this.agents = new HashTable<>();
        this.operations = new LinkedList<>();
        this.visits = new LinkedList<>();
        this.alerts = new ArrayList<>();
    }
}
