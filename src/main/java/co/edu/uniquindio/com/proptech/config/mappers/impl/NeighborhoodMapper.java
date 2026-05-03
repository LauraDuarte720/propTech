package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;

public class NeighborhoodMapper implements MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> {

    @Override
    public Neighborhood toEntity(NeighborhoodDtoCreate dto) {
        return Neighborhood.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .zone(dto.getZone())
                .build();
    }

    @Override
    public NeighborhoodDtoReturn toDto(Neighborhood entity) {
        return NeighborhoodDtoReturn.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .zone(entity.getZone())
                .build();
    }

    @Override
    public Neighborhood toUpdate(NeighborhoodDtoUpdate dto) {
        return Neighborhood.builder()
                .name(dto.getName())
                .city(dto.getCity())
                .zone(dto.getZone())
                .build();
    }
}