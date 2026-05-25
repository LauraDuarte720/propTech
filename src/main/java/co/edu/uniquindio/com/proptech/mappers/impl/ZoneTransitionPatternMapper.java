package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.domain.dtos.ZoneTransitionPatternDto;
import co.edu.uniquindio.com.proptech.domain.model.ZoneTransitionPattern;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import org.springframework.stereotype.Component;

@Component
public class ZoneTransitionPatternMapper implements MapperOnlyDto<ZoneTransitionPattern, ZoneTransitionPatternDto> {

    private final ZoneNodeMapper zoneNodeMapper;

    public ZoneTransitionPatternMapper(ZoneNodeMapper zoneNodeMapper) {
        this.zoneNodeMapper = zoneNodeMapper;
    }

    @Override
    public ZoneTransitionPatternDto toDto(ZoneTransitionPattern entity) {
        return ZoneTransitionPatternDto.builder()
                .from(zoneNodeMapper.toDto(entity.getFrom()))
                .to(zoneNodeMapper.toDto(entity.getTo()))
                .weight(entity.getWeight())
                .operationCount(entity.getOperationCount())
                .build();
    }
}