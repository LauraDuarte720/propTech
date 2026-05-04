package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.services.NeighborhoodService;
import org.springframework.stereotype.Component;

@Component
public class GeographicZoneMapper implements MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> {
    MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper;
    NeighborhoodService neighborhoodService;

    public GeographicZoneMapper(MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper, NeighborhoodService neighborhoodService) {
        this.neighborhoodMapper = neighborhoodMapper;
        this.neighborhoodService = neighborhoodService;
    }

    @Override
    public GeographicZone toEntity(GeographicZoneDtoCreate dto) {
        return GeographicZone.builder()
                .city(dto.getCity())
                .zone(dto.getZone())
                .neighborhood(dto.getNeighborhoodId() == null ? null : neighborhoodService.getNeighborhoodById(dto.getNeighborhoodId()))
                .build();
    }

    @Override
    public GeographicZoneDtoReturn toDto(GeographicZone entity) {
        return GeographicZoneDtoReturn.builder()
                .id(entity.getId())
                .city(entity.getCity())
                .zone(entity.getZone())
                .neighborhood(neighborhoodMapper.toDto(entity.getNeighborhood()))
                .build();
    }

    @Override
    public GeographicZone toUpdate(GeographicZoneDtoUpdate dto) {
        return GeographicZone.builder()
                .city(dto.getCity())
                .zone(dto.getZone())
                .neighborhood(dto.getNeighborhoodId() == null ? null : neighborhoodService.getNeighborhoodById(dto.getNeighborhoodId()))
                .build();
    }
}