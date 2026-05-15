package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.ClientDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper implements MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> {

    @Override
    public Client toEntity(ClientDtoCreate dto) {
        return Client.builder()
                .cedula(dto.getCedula())
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .budget(dto.getBudget())
                .minBedrooms(dto.getMinBedrooms())
                .clientType(dto.getClientType())
                .searchStatus(dto.getSearchStatus())
                .desiredPropertyType(dto.getDesiredPropertyType())
                .build();
    }

    @Override
    public ClientDtoReturn toDto(Client entity) {
        return ClientDtoReturn.builder()
                .cedula(entity.getCedula())
                .name(entity.getName())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .budget(entity.getBudget())
                .minBedrooms(entity.getMinBedrooms())
                .clientType(entity.getClientType())
                .searchStatus(entity.getSearchStatus())
                .desiredPropertyType(entity.getDesiredPropertyType())
                .build();
    }

    @Override
    public Client toUpdate(ClientDtoUpdate dto) {
        return Client.builder()
                .cedula(dto.getCedula()) // obligatorio
                .name(dto.getName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .budget(dto.getBudget())
                .minBedrooms(dto.getMinBedrooms())
                .clientType(dto.getClientType())
                .searchStatus(dto.getSearchStatus())
                .desiredPropertyType(dto.getDesiredPropertyType())
                .build();
    }
}