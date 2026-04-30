package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    private final PropTech propTech;

    public ClientRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public void save(Client client) {
        propTech.addUpdateClient(client);
    }

    @Override
    public Optional<Client> findByCedula(String cedula) {
        return Optional.ofNullable(propTech.getClient(cedula));
    }

    @Override
    public boolean deleteById(String cedula) {
        return propTech.removeClient(cedula);
    }
}