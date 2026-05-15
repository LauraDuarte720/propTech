package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.SupportRequestDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.SupportRequestDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.SupportRequest;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.stereotype.Component;



@Component
public class SupportRequestMapper  implements MapperCreate<SupportRequest, SupportRequestDtoCreate, SupportRequestDtoReturn> {

    ClientService clientService;
    AgentService agentService;
    PropertyService propertyService;
    PropertyMapper propertyMapper;
    AgentMapper agentMapper;
    ClientMapper clientMapper;

    public SupportRequestMapper(ClientService clientService, AgentService agentService, PropertyService propertyService, PropertyMapper propertyMapper, AgentMapper agentMapper, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.agentService = agentService;
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
        this.agentMapper = agentMapper;
        this.clientMapper = clientMapper;

    }

    public SupportRequest toEntity(SupportRequestDtoCreate dto) {
        return SupportRequest.builder()
                .client(clientService.getClientByCedula(dto.getClientId()))
                .property(propertyService.getPropertyByCode(dto.getPropertyId()))
                .agent(agentService.getAgentByCedula(dto.getAgentId()))
                .message(dto.getMessage())
                .build();
    }

    @Override
    public SupportRequestDtoReturn toDto(SupportRequest entity) {
        return SupportRequestDtoReturn.builder()
                .client(clientMapper.toDto(entity.getClient()))
                .property(propertyMapper.toDto(entity.getProperty()))
                .agent(agentMapper.toDto(entity.getAgent()))
                .message(entity.getMessage())
                .build();
    }

}