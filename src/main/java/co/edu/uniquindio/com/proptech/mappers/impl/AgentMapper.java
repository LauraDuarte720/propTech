package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.services.GeographicZoneService;
import org.springframework.stereotype.Component;

@Component
public class AgentMapper implements MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> {
    GeographicZoneService geographicZoneService;

    MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper;

    public AgentMapper(MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper, GeographicZoneService geographicZoneService) {
        this.geographicZoneMapper = geographicZoneMapper;
        this.geographicZoneService = geographicZoneService;
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
                .assignedZone(geographicZoneService.getGeographicZoneById(dto.getAssignedZoneId()))
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
                .assignedZone(geographicZoneMapper.toDto(agent.getAssignedZone()))
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
                .assignedZone(dto.getAssignedZoneId() == null ? null : geographicZoneService.getGeographicZoneById(dto.getAssignedZoneId()))
                .build();
    }
}