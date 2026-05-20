package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AlgorithmRepository;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.stereotype.Repository;

@Repository
public class AlgorithmRepositoryImpl implements AlgorithmRepository {

    private final PropTech propTech;

    public AlgorithmRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Graph<Object> getClientPropertyGraph() {
        return propTech.getClientPropertyGraph();
    }

    @Override
    public Graph<GeographicZone> getZoneGraph() {
        return propTech.getZoneGraph();
    }

    @Override
    public HashTable<String, Integer> getVisitFrequencyByProperty() {
        return propTech.getVisitFrequencyByProperty();
    }
}
