package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperSimple;
import co.edu.uniquindio.com.proptech.domain.dtos.BasicAlertDto;
import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;

public class BasicAlertMapper implements MapperSimple<BasicAlert, BasicAlertDto> {

    @Override
    public BasicAlertDto toDto(BasicAlert entity) {
        return BasicAlertDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .reviewed(entity.isReviewed())
                .alertType(entity.getAlertType())
                .agent(null)
                .client(null)
                .operation(null)
                .property(null)
                .visit(null)
                .build();
    }
}