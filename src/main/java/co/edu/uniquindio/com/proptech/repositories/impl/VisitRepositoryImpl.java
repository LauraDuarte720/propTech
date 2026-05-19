package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.repositories.VisitRepository;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VisitRepositoryImpl implements VisitRepository {

    private final PropTech propTech;

    public VisitRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Visit save(Visit visit) {
        propTech.getVisits().addLast(visit);
        incrementFrequency(visit);
        return visit;
    }

    @Override
    public Optional<Visit> findById(String id) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            if (propTech.getVisits().get(i).getId().equals(id))
                return Optional.of(propTech.getVisits().get(i));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            Visit visit = propTech.getVisits().get(i);
            if (visit.getId().equals(id)) {
                decrementFrequency(visit);
                propTech.getVisits().removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Visit update(Visit visit) {
        for (int i = 0; i < propTech.getVisits().size(); i++) {
            Visit existing = propTech.getVisits().get(i);
            if (existing.getId().equals(visit.getId())) {
                if (!existing.getProperty().getCode().equals(visit.getProperty().getCode())) {
                    decrementFrequency(existing);
                    incrementFrequency(visit);
                }
                propTech.getVisits().set(i, visit);
                return visit;
            }
        }
        return visit;
    }

    @Override
    public LinkedList<Visit> getAllVisits() {
        return propTech.getVisits();
    }

    @Override
    public LinkedList<Visit> getVisitsByProperty(String propertyCode) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getProperty() != null && visit.getProperty().getCode().equals(propertyCode))
                result.addLast(visit);
        }
        return result;
    }

    @Override
    public LinkedList<Visit> getVisitsByClient(String clientCedula) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getClient() != null && visit.getClient().getCedula().equals(clientCedula))
                result.addLast(visit);
        }
        return result;
    }

    @Override
    public LinkedList<Visit> getVisitsByAgent(String agentCedula) {
        LinkedList<Visit> result = new LinkedList<>();
        for (Visit visit : propTech.getVisits()) {
            if (visit.getAgent() != null && visit.getAgent().getCedula().equals(agentCedula))
                result.addLast(visit);
        }
        return result;
    }

    @Override
    public HashTable<String, Integer> getVisitFrequencyByProperty() {
        return propTech.getVisitFrequencyByProperty();
    }

    @Override
    public HashTable<City, Integer> getVisitFrequencyByCity() {
        return propTech.getVisitFrequencyByCity();
    }

    @Override
    public HashTable<City, HashTable<Zone, Integer>> getVisitFrequencyByCityZone() {
        return propTech.getVisitFrequencyByCityZone();
    }

    @Override
    public HashTable<City, HashTable<Zone, HashTable<String, Integer>>> getVisitsFrequenciesByCityZoneNeighbor() {
        return propTech.getVisitsFrequenciesByCityZoneNeighbor();
    }


    public HashTable<City, HashTable<Zone, HashTable<String, Integer>>> getVisitsFrequenciesByZone() {
        return propTech.getVisitsFrequenciesByCityZoneNeighbor();
    }

    private void incrementFrequency(Visit visit) {
        String propertyCode = visit.getProperty().getCode();
        Integer freqProp = propTech.getVisitFrequencyByProperty().get(propertyCode);
        propTech.getVisitFrequencyByProperty().put(propertyCode, freqProp == null ? 1 : freqProp + 1);
        Neighborhood neighborhood = visit.getProperty().getNeighborhood();
        City city = neighborhood.getCity();
        Zone zone = neighborhood.getZone();
        String neighborhoodName = neighborhood.getName();

        HashTable<Zone, HashTable<String, Integer>> byZone = propTech.getVisitsFrequenciesByCityZoneNeighbor().get(city);
        if (byZone == null) {
            byZone = new HashTable<>();
            propTech.getVisitsFrequenciesByCityZoneNeighbor().put(city, byZone);
        }
        HashTable<String, Integer> byNeighborhood = byZone.get(zone);
        if (byNeighborhood == null) {
            byNeighborhood = new HashTable<>();
            byZone.put(zone, byNeighborhood);
        }
        Integer freqNeighborhood = byNeighborhood.get(neighborhoodName);
        byNeighborhood.put(neighborhoodName, freqNeighborhood == null ? 1 : freqNeighborhood + 1);


        Integer freqCity = propTech.getVisitFrequencyByCity().get(city);
        propTech.getVisitFrequencyByCity().put(city, freqCity == null ? 1 : freqCity + 1);


        HashTable<Zone, Integer> zoneMap = propTech.getVisitFrequencyByCityZone().get(city);
        if (zoneMap == null) {
            zoneMap = new HashTable<>();
            propTech.getVisitFrequencyByCityZone().put(city, zoneMap);
        }
        Integer freqZone = zoneMap.get(zone);
        zoneMap.put(zone, freqZone == null ? 1 : freqZone + 1);
    }


    private void decrementFrequency(Visit visit) {
        String propertyCode = visit.getProperty().getCode();
        Integer freqProp = propTech.getVisitFrequencyByProperty().get(propertyCode);
        if (freqProp != null) {
            if (freqProp <= 1) propTech.getVisitFrequencyByProperty().remove(propertyCode);
            else propTech.getVisitFrequencyByProperty().put(propertyCode, freqProp - 1);
        }

        Neighborhood neighborhood = visit.getProperty().getNeighborhood();
        City city = neighborhood.getCity();
        Zone zone = neighborhood.getZone();
        String neighborhoodName = neighborhood.getName();


        HashTable<Zone, HashTable<String, Integer>> byZone = propTech.getVisitsFrequenciesByCityZoneNeighbor().get(city);
        if (byZone != null) {
            HashTable<String, Integer> byNeighborhood = byZone.get(zone);
            if (byNeighborhood != null) {
                Integer freqNeighborhood = byNeighborhood.get(neighborhoodName);
                if (freqNeighborhood != null) {
                    if (freqNeighborhood <= 1) byNeighborhood.remove(neighborhoodName);
                    else byNeighborhood.put(neighborhoodName, freqNeighborhood - 1);
                }
            }
        }

        Integer freqCity = propTech.getVisitFrequencyByCity().get(city);
        if (freqCity != null) {
            if (freqCity <= 1) propTech.getVisitFrequencyByCity().remove(city);
            else propTech.getVisitFrequencyByCity().put(city, freqCity - 1);
        }

        HashTable<Zone, Integer> zoneMap = propTech.getVisitFrequencyByCityZone().get(city);
        if (zoneMap != null) {
            Integer freqZone = zoneMap.get(zone);
            if (freqZone != null) {
                if (freqZone <= 1) zoneMap.remove(zone);
                else zoneMap.put(zone, freqZone - 1);
            }
        }
    }
}