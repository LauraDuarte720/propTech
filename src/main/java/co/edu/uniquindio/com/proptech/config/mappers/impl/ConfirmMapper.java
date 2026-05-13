package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.config.mappers.MapperOnlyEntity;
import co.edu.uniquindio.com.proptech.domain.dtos.ConfirmDto;
import org.springframework.stereotype.Component;

@Component
public class ConfirmMapper implements MapperOnlyEntity<Boolean, ConfirmDto> {

    public Boolean toEntity(ConfirmDto dto) {
        return dto.confirm();
    }
}