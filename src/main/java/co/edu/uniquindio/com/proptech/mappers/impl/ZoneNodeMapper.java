package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.domain.dtos.ZoneNodeDto;
import co.edu.uniquindio.com.proptech.domain.model.ZoneNode;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import org.springframework.stereotype.Component;

@Component
public class ZoneNodeMapper implements MapperOnlyDto<ZoneNode, ZoneNodeDto> {
    @Override
    public ZoneNodeDto toDto(ZoneNode entity) {
        if (entity == null) return null;
        return ZoneNodeDto.builder()
                .level(entity.getLevel() != null ? entity.getLevel().name() : null)
                .city(entity.getCity() != null ? entity.getCity().toString() : null)
                .zone(entity.getZone() != null ? entity.getZone().toString() : null)
                .neighborhoodName(entity.getNeighborhoodName())
                .label(entity.getLabel())
                .build();
    }
}