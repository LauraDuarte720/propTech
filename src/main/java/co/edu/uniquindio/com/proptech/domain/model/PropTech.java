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
    private ArrayList<GeographicZone> geographicZones;

    public PropTech(String NIT) {
        this.NIT = NIT;
        this.properties = new HashTable<>();
        this.clients = new HashTable<>();
        this.agents = new HashTable<>();
        this.operations = new LinkedList<>();
        this.visits = new LinkedList<>();
        this.alerts = new ArrayList<>();
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


    public Alert addAlert(Alert alert) {
        alerts.add(alert);
        return alert;
    }

    public Alert getAlert(String id) {
        for (int i = 0; i < alerts.size(); i++) {
            Alert a = alerts.get(i);
            if (a.getId().equals(id)) return a;
        }
        return null;
    }

    public boolean removeAlert(String id) {
        for (int i = 0; i < alerts.size(); i++) {
            if (alerts.get(i).getId().equals(id)) {
                alerts.remove(i);
                return true;
            }
        }
        return false;
    }

    public Alert updateAlert(Alert alert) {
        for (int i = 0; i < alerts.size(); i++) {
            if (alerts.get(i).getId().equals(alert.getId())) {
                alerts.set(i, alert);

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
}