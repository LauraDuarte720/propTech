package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.GeographicZoneDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.GeographicZoneDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.GeographicZoneDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import org.springframework.stereotype.Component;

@Component
public class GeographicZoneMapper implements MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> {

    @Override
    public GeographicZone toEntity(GeographicZoneDtoCreate dto) {
        return GeographicZone.builder()
                .city(dto.getCity())
                .zone(dto.getZone())
                .neighborhood(null)
                .build();
    }

    @Override
    public GeographicZoneDtoReturn toDto(GeographicZone entity) {
        return GeographicZoneDtoReturn.builder()
                .id(entity.getId())
                .city(entity.getCity())
                .zone(entity.getZone())
                .neighborhood(null)
                .build();
    }

    @Override
    public GeographicZone toUpdate(GeographicZoneDtoUpdate dto) {
        return GeographicZone.builder()
                .city(dto.getCity())
                .zone(dto.getZone())
                .neighborhood(null)
                .build();
    }
}