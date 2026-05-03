package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.Mapper;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Agent;

public class AgentMapper implements Mapper<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> {

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
    public AgentDtoReturn toDto(Agent entity) {
        return AgentDtoReturn.builder()
                .cedula(entity.getCedula())
                .name(entity.getName())
                .username(entity.getUsername())
                .contact(entity.getContact())
                .closedDeals(entity.getClosedDeals())
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