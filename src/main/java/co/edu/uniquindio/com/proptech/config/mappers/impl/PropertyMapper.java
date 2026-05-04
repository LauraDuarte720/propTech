package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.NeighborhoodService;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper implements MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> {

    MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper;
    NeighborhoodService neighborhoodService;
    AgentService agentService;

    public PropertyMapper(MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper, MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper, NeighborhoodService neighborhoodService, AgentService agentService) {
        this.agentMapper = agentMapper;
        this.neighborhoodMapper = neighborhoodMapper;
        this.neighborhoodService = neighborhoodService;
        this.agentService = agentService;
    }


    @Override
    public Property toEntity(PropertyDtoCreate dto) {
        return Property.builder()
                .address(dto.getAddress())
                .neighborhood(neighborhoodService.getNeighborhoodById(dto.getNeighborhoodId()))
                .propertyType(dto.getPropertyType())
                .purpose(dto.getPurpose())
                .price(dto.getPrice())
                .area(dto.getArea())
                .numBedrooms(dto.getNumBedrooms())
                .numBathrooms(dto.getNumBathrooms())
                .status(dto.getStatus())
                .available(dto.isAvailable())
                .build();
    }

    @Override
    public PropertyDtoReturn toDto(Property entity) {
        return PropertyDtoReturn.builder()
                .code(entity.getCode())
                .address(entity.getAddress())
                .neighborhood(neighborhoodMapper.toDto(entity.getNeighborhood()))
                .propertyType(entity.getPropertyType())
                .purpose(entity.getPurpose())
                .price(entity.getPrice())
                .area(entity.getArea())
                .numBedrooms(entity.getNumBedrooms())
                .numBathrooms(entity.getNumBathrooms())
                .status(entity.getStatus())
                .available(entity.isAvailable())
                .agent(agentMapper.toDto(entity.getAgent()))
                .build();
    }

    @Override
    public Property toUpdate(PropertyDtoUpdate dto) {
        return Property.builder()
                .code(dto.getCode())
                .address(dto.getAddress())
                .neighborhood(dto.getNeighborhoodId() == null ? null : neighborhoodService.getNeighborhoodById(dto.getNeighborhoodId()))
                .propertyType(dto.getPropertyType())
                .purpose(dto.getPurpose())
                .price(dto.getPrice())
                .area(dto.getArea())
                .numBedrooms(dto.getNumBedrooms())
                .numBathrooms(dto.getNumBathrooms())
                .status(dto.getStatus())
                .available(dto.getAvailable())
                .agent(dto.getAgentId() == null ? null : agentService.getAgentByCedula(dto.getAgentId()))
                .build();
    }
}