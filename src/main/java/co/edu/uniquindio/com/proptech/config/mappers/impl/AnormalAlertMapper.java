package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperSimple;
import co.edu.uniquindio.com.proptech.domain.dtos.AnormalAlertDto;
import co.edu.uniquindio.com.proptech.domain.model.AnormalAlert;
import org.springframework.stereotype.Component;

@Component
public class AnormalAlertMapper implements MapperSimple<AnormalAlert, AnormalAlertDto> {
    @Override
    public AnormalAlertDto toDto(AnormalAlert entity) {
        return AnormalAlertDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .reviewed(entity.isReviewed())
                .alertAnormalType(entity.getAlertAnormalType())
                .attentionLevel(entity.getAttentionLevel())
                .agent(null)
                .client(null)
                .operation(null)
                .property(null)
                .visit(null)
                .build();
    }
}
