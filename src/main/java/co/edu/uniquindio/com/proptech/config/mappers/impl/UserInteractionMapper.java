package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.UserInteractionDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.UserInteractionDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.UserInteraction;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserInteractionMapper implements MapperCreate<UserInteraction, UserInteractionDtoCreate, UserInteractionDtoReturn> {

    private final ClientMapper clientMapper;
    private final PropertyMapper propertyMapper;
    private final ClientService clientService;
    private final PropertyService propertyService;

    public UserInteractionMapper(ClientMapper clientMapper, PropertyMapper propertyMapper, ClientService clientService, PropertyService propertyService) {
        this.clientMapper = clientMapper;
        this.propertyMapper = propertyMapper;
        this.clientService = clientService;
        this.propertyService = propertyService;
    }

    @Override
    public UserInteraction toEntity(UserInteractionDtoCreate dto) {
        return UserInteraction.builder()
                .interactionType(dto.getInteractionType())
                .client(clientService.getClientByCedula(dto.getClientId()))
                .property(propertyService.getPropertyByCode(dto.getPropertyId()))
                .build();
    }

    @Override
    public UserInteractionDtoReturn toDto(UserInteraction entity) {
        return UserInteractionDtoReturn.builder()
                .id(entity.getId())
                .interactionType(entity.getInteractionType())
                .timestamp(entity.getTimestamp())
                .client(clientMapper.toDto(entity.getClient()))
                .property(propertyMapper.toDto(entity.getProperty()))
                .build();
    }
}