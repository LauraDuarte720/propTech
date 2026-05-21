package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.structures.graph.Graph;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;

public interface AlgorithmRepository {
    Graph<Object> getClientPropertyGraph();
    Graph<GeographicZone> getZoneGraph();
}
