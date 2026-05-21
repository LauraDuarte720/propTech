package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper implements MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> {

    @Override
    public Visit toEntity(VisitDtoCreate dto) {
        return Visit.builder()
                .date(dto.getDate())
                .postVisitNotes(dto.getPostVisitNotes())
                .build();
    }

    @Override
    public VisitDtoReturn toDto(Visit entity) {
        return VisitDtoReturn.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .status(entity.getStatus())
                .postVisitNotes(entity.getPostVisitNotes())
                .build();
    }

    @Override
    public Visit toUpdate(VisitDtoUpdate dto) {
        return Visit.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .status(dto.getStatus())
                .postVisitNotes(dto.getPostVisitNotes())
                .build();
    }
}