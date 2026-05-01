package co.edu.uniquindio.com.proptech.repositories.impl;

import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.PropTech;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import java.util.Optional;

public class AgentRepositoryImpl implements AgentRepository {

    private final PropTech propTech;

    public AgentRepositoryImpl(PropTech propTech) {
        this.propTech = propTech;
    }

    @Override
    public Agent save(Agent agent) {
        return propTech.addUpdateAgent(agent);
    }

    @Override
    public Optional<Agent> findByCedula(String cedula) {
        return Optional.ofNullable(propTech.getAgent(cedula));
    }

    @Override
    public boolean deleteById(String cedula) {
        return propTech.removeAgent(cedula);
    }
}