package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.PropertyDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.PropertyDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.PropertyDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Property;

public class PropertyMapper implements MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> {

    @Override
    public Property toEntity(PropertyDtoCreate dto) {
        return Property.builder()
                .address(dto.getAddress())
                .neighborhood(null)
                .propertyType(dto.getPropertyType())
                .purpose(dto.getPurpose())
                .price(dto.getPrice())
                .area(dto.getArea())
                .numBedrooms(dto.getNumBedrooms())
                .numBathrooms(dto.getNumBathrooms())
                .status(dto.getStatus())
                .available(dto.isAvailable())
                .agent(null)
                .build();
    }

    @Override
    public PropertyDtoReturn toDto(Property entity) {
        return PropertyDtoReturn.builder()
                .code(entity.getCode())
                .address(entity.getAddress())
                .neighborhood(null)
                .propertyType(entity.getPropertyType())
                .purpose(entity.getPurpose())
                .price(entity.getPrice())
                .area(entity.getArea())
                .numBedrooms(entity.getNumBedrooms())
                .numBathrooms(entity.getNumBathrooms())
                .status(entity.getStatus())
                .available(entity.isAvailable())
                .agent(null)
                .build();
    }

    @Override
    public Property toUpdate(PropertyDtoUpdate dto) {
        return Property.builder()
                .code(dto.getCode())
                .address(dto.getAddress())
                .neighborhood(null)
                .propertyType(dto.getPropertyType())
                .purpose(dto.getPurpose())
                .price(dto.getPrice())
                .area(dto.getArea())
                .numBedrooms(dto.getNumBedrooms())
                .numBathrooms(dto.getNumBathrooms())
                .status(dto.getStatus())
                .available(dto.getAvailable())
                .agent(null)
                .build();
    }
}