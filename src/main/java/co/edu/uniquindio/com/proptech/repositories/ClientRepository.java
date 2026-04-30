package co.edu.uniquindio.com.proptech.repositories;

import co.edu.uniquindio.com.proptech.domain.model.Client;
import java.util.Optional;

public interface ClientRepository {
    void save(Client client);
    Optional<Client> findByCedula(String cedula);
    boolean deleteById(String cedula);
}