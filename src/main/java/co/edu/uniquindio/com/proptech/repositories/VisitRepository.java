package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.Zone;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;

import java.util.Optional;

public interface VisitRepository {
    Visit save(Visit visit);
    Optional<Visit> findById(String id);
    boolean deleteById(String id);
    Visit update(Visit visit);
    LinkedList<Visit> getAllVisits();
    LinkedList<Visit> getVisitsByProperty(String propertyCode);
    LinkedList<Visit> getVisitsByClient(String clientCedula);
    LinkedList<Visit> getVisitsByAgent(String agentCedula);
    HashTable<String, Integer> getVisitFrequencyByProperty();
    HashTable<City, Integer> getVisitFrequencyByCity();
    HashTable<City, HashTable<Zone, Integer>> getVisitFrequencyByCityZone();
    HashTable<City, HashTable<Zone, HashTable<String, Integer>>> getVisitsFrequenciesByCityZoneNeighbor();
}