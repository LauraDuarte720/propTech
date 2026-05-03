package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import org.springframework.stereotype.Component;

@Component
public class AgentMapper implements MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> {

    MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper;

    public AgentMapper(MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper) {
        this.geographicZoneMapper = geographicZoneMapper;
    }


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
                .assignedZone(geographicZoneMapper.toDto(agent.getAssignedZone())) // map separately if needed
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