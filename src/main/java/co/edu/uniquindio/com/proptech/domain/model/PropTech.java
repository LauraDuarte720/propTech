package co.edu.uniquindio.com.proptech.domain.model;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.structures.AVLTree.AVLTree;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.queue.Queue;
import co.edu.uniquindio.com.proptech.structures.stack.Stack;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class PropTech {

    public PropTech(String NIT) {
        this.NIT = NIT;
    }

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
    private AVLTree<Property> propertiesTree = new AVLTree<>();
    private AVLTree<Client> clientsTree = new AVLTree<>();
    private AVLTree<Agent> agentsTree = new AVLTree<>();
    private HashTable<String, Integer> visitFrequencyByProperty = new HashTable<>();
    private HashTable<City, Integer> visitFrequencyByCity = new HashTable<>();
    private HashTable<City, HashTable<Zone, Integer>> visitFrequencyByCityZone = new HashTable<>();
    private HashTable<City, HashTable<Zone, HashTable<String, Integer>>> visitsFrequenciesByCityZoneNeighbor = new HashTable<>();
    private HashTable<City, ArrayList<Property>> propertiesByCity = new HashTable<>();
    private HashTable<PropertyType, ArrayList<Property>> propertiesByType = new HashTable<>();
    private HashTable<PropertyStatus, ArrayList<Property>> propertiesByStatus = new HashTable<>();
    private Graph<Object> clientPropertyGraph = new Graph<>();
    private Graph<GeographicZone> zoneGraph = new Graph<>();
    private Stack<AdminActionLog> adminUndoHistory = new Stack<>();
    private Queue<AdminActionLog> adminActionHistory = new Queue<>();

    @PostConstruct
    public void initBuckets() {
        for (PropertyStatus status : PropertyStatus.values()) {
            propertiesByStatus.put(status, new ArrayList<>());
        }
        for (PropertyType type : PropertyType.values()) {
            propertiesByType.put(type, new ArrayList<>());
        }
        for (City city : City.values()) {
            propertiesByCity.put(city, new ArrayList<>());
        }
    }
}