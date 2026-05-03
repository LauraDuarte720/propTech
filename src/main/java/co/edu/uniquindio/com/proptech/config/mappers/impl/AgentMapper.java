package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Agent;

public class AgentMapper implements MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> {

    @Override
    public Agent toEntity(AgentDtoCreate dto) {
        return Agent.builder()
                .cedula(dto.getCedula())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .contact(dto.getContact())
                .closedDeals(dto.getClosedDeals())
                .assignedZone(null) // map separately if needed (GeographicZoneMapper)
                .build();
    }

    @Override
    public AgentDtoReturn toDto(Agent agent) {
        return AgentDtoReturn.builder()
                .cedula(agent.getCedula())
                .name(agent.getName())
                .username(agent.getUsername())
                .contact(agent.getContact())
                .closedDeals(agent.getClosedDeals())
                .assignedZone(null) // map separately if needed
                .build();
    }

    @Override
    public Agent toUpdate(AgentDtoUpdate dto) {
        return Agent.builder()
                .cedula(dto.getCedula())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .contact(dto.getContact())
                .closedDeals(dto.getClosedDeals())
                .assignedZone(null) // map separately if needed (GeographicZoneMapper)
                .build();
    }
}