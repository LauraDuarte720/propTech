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
@Builder
@Component
public class PropTech {

    private String NIT;
    private HashTable<String, Property> properties;
    private HashTable<String, Client> clients;
    private HashTable<String, Agent> agents;
    private LinkedList<Operation> operations;
    private LinkedList<Visit> visits;
    private ArrayList<BasicAlert> basicAlerts;
    private ArrayList<AbnormalAlert> abnormalAlerts;
    private ArrayList<GeographicZone> geographicZones;
    private ArrayList<Neighborhood> neighborhoods;

    public PropTech(String NIT) {
        this.NIT = NIT;
        this.properties = new HashTable<>();
        this.clients = new HashTable<>();
        this.agents = new HashTable<>();
        this.operations = new LinkedList<>();
        this.visits = new LinkedList<>();
        this.basicAlerts = new ArrayList<>();
        this.abnormalAlerts = new ArrayList<>();
        this.geographicZones = new ArrayList<>();
    }


    public Property addUpdateProperty(Property property) {

        properties.put(property.getCode(), property);
        return property;
    }

    public Property getProperty(String code) {
        return properties.get(code);
    }

    public boolean removeProperty(String code) {
        return properties.remove(code);
    }


    public Client addUpdateClient(Client client) {
        clients.put(client.getCedula(), client);
        return client;
    }

    public Client getClient(String id) {
        return clients.get(id);
    }


    public boolean removeClient(String id) {
        return clients.remove(id);
    }


    public Agent addUpdateAgent(Agent agent) {
        agents.put(agent.getCedula(), agent);
        return agent;
    }

    public Agent getAgent(String id) {
        return agents.get(id);
    }

    public boolean removeAgent(String id) {
        return agents.remove(id);
    }


    public Operation addOperation(Operation operation) {
        operations.addLast(operation);
        return operation;
    }

    public Operation getOperation(String id) {
        for (int i = 0; i < operations.size(); i++) {
            Operation op = operations.get(i);
            if (op.getId().equals(id)) return op;
        }
        return null;
    }

    public boolean removeOperation(String id) {
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getId().equals(id)) {
                operations.removeAt(i);
                return true;
            }
        }
        return false;
    }

    public Operation updateOperation(Operation operation) {
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getId().equals(operation.getId())) {
                operations.set(i, operation);
            }
        }
        return operation;
    }


    public Visit addVisit(Visit visit) {
        visits.addLast(visit);
        return visit;
    }

    public Visit getVisit(String id) {
        for (int i = 0; i < visits.size(); i++) {
            Visit v = visits.get(i);
            if (v.getId().equals(id)) return v;
        }
        return null;
    }

    public boolean removeVisit(String id) {
        for (int i = 0; i < visits.size(); i++) {
            if (visits.get(i).getId().equals(id)) {
                visits.removeAt(i);
                return true;
            }
        }
        return false;
    }

    public Visit updateVisit(Visit visit) {
        for (int i = 0; i < visits.size(); i++) {
            if (visits.get(i).getId().equals(visit.getId())) {
                visits.set(i, visit);

            }
        }
        return visit;
    }


    public BasicAlert addBasicAlert(BasicAlert alert) {
        basicAlerts.add(alert);
        return alert;
    }

    public BasicAlert getBasicAlert(String id) {
        for (int i = 0; i < basicAlerts.size(); i++) {
            if (basicAlerts.get(i).getId().equals(id)) {
                return basicAlerts.get(i);
            }
        }
        return null;
    }

    public boolean removeBasicAlert(String id) {
        for (int i = 0; i < basicAlerts.size(); i++) {
            if (basicAlerts.get(i).getId().equals(id)) {
                basicAlerts.remove(i);
                return true;
            }
        }
        return false;
    }

    public BasicAlert updateBasicAlert(BasicAlert alert) {
        for (int i = 0; i < basicAlerts.size(); i++) {
            if (basicAlerts.get(i).getId().equals(alert.getId())) {
                basicAlerts.set(i, alert);
            }
        }
        return alert;
    }

    public AbnormalAlert addAbnormalAlert(AbnormalAlert alert) {
        abnormalAlerts.add(alert);
        return alert;
    }

    public AbnormalAlert getAbnormalAlert(String id) {
        for (int i = 0; i < abnormalAlerts.size(); i++) {
            if (abnormalAlerts.get(i).getId().equals(id)) {
                return abnormalAlerts.get(i);
            }
        }
        return null;
    }

    public boolean removeAbnormalAlert(String id) {
        for (int i = 0; i < abnormalAlerts.size(); i++) {
            if (abnormalAlerts.get(i).getId().equals(id)) {
                abnormalAlerts.remove(i);
                return true;
            }
        }
        return false;
    }

    public AbnormalAlert updateAbnormalAlert(AbnormalAlert alert) {
        for (int i = 0; i < abnormalAlerts.size(); i++) {
            if (abnormalAlerts.get(i).getId().equals(alert.getId())) {
                abnormalAlerts.set(i, alert);
            }
        }
        return alert;
    }

    public GeographicZone addGeographicZone(GeographicZone zone) {
        geographicZones.add(zone);
        return zone;
    }

    public GeographicZone updateGeographicZone(GeographicZone zone) {
        for (int i = 0; i < geographicZones.size(); i++) {
            if (geographicZones.get(i).getId().equals(zone.getId())) {
                geographicZones.set(i, zone);
            }
        }
        return zone;
    }

    public boolean removeGeographicZone(String id) {
        for (int i = 0; i < geographicZones.size(); i++) {
            if (geographicZones.get(i).getId().equals(id)) {
                geographicZones.remove(i);
                return true;
            }
        }
       return false;
    }

    public GeographicZone getGeographicZone(String id) {
        for (int i = 0; i < geographicZones.size(); i++) {
            if (geographicZones.get(i).getId().equals(id)) {
                return geographicZones.get(i);
            }
        }
        return null;
    }

    public Neighborhood addNeighborhood(Neighborhood neighborhood) {
        neighborhoods.add(neighborhood);
        return neighborhood;
    }

    public Neighborhood getNeighborhood(String id) {
        for (int i = 0; i < neighborhoods.size(); i++) {
            if (neighborhoods.get(i).getId().equals(id)) {
                return neighborhoods.get(i);
            }
        }
        return null;
    }

    public boolean removeNeighborhood(String id) {
        for (int i = 0; i < neighborhoods.size(); i++) {
            if (neighborhoods.get(i).getId().equals(id)) {
                neighborhoods.remove(i);
                return true;
            }
        }
        return false;
    }

    public Neighborhood updateNeighborhood(Neighborhood neighborhood) {
        for (int i = 0; i < neighborhoods.size(); i++) {
            if (neighborhoods.get(i).getId().equals(neighborhood.getId())) {
                neighborhoods.set(i, neighborhood);
            }
        }
        return neighborhood;
    }
}