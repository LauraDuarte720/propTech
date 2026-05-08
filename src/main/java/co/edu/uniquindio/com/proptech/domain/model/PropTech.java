package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PropTech {

    private String NIT;
    private HashTable<String, Property> properties = new HashTable<>();
    private HashTable<String, Client> clients = new HashTable<>();
    private HashTable<String, Agent> agents = new HashTable<>();
    private LinkedList<Operation> operations = new LinkedList<>();
    private LinkedList<Visit> visits = new LinkedList<>();
    private ArrayList<BasicAlert> basicAlerts = new ArrayList<>();
    private ArrayList<AbnormalAlert> abnormalAlerts = new ArrayList<>();
    private ArrayList<GeographicZone> geographicZones = new ArrayList<>();
    private ArrayList<Neighborhood> neighborhoods = new ArrayList<>();

    public PropTech(String NIT) {
        this.NIT = NIT;
    }
}