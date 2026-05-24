package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Operation;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper implements MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> {

    MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;
    MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    AgentService agentService;
    ClientService clientService;
    PropertyService propertyService;

    public OperationMapper(MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper, MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper, MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper, AgentService agentService, ClientService clientService, PropertyService propertyService) {
        this.agentMapper = agentMapper;
        this.clientMapper = clientMapper;
        this.propertyMapper = propertyMapper;
        this.agentService = agentService;
        this.clientService = clientService;
        this.propertyService = propertyService;
    }

    @Override
    public Operation toEntity(OperationDtoCreate dto) {
        return Operation.builder()
                .property(propertyService.getPropertyByCode(dto.getPropertyId()))
                .client(clientService.getClientByCedula(dto.getClientId()))
                .agent(agentService.getAgentByCedula(dto.getAgentId()))
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal() == null ? null : dto.getDateFinal())
                .operationType(dto.getOperationType())
                .commission(dto.getCommission())
                .build();
    }

    @Override
    public OperationDtoReturn toDto(Operation entity) {
        return OperationDtoReturn.builder()
                .id(entity.getId())
                .property(propertyMapper.toDto(entity.getProperty()))
                .client(clientMapper.toDto(entity.getClient()))
                .agent(agentMapper.toDto(entity.getAgent()))
                .dateInitial(entity.getDateInitial())
                .dateFinal(entity.getDateFinal())
                .operationType(entity.getOperationType())
                .value(entity.getValue())
                .commission(entity.getCommission())
                .processStatus(entity.getProcessStatus())
                .build();
    }

    @Override
    public Operation toUpdate(OperationDtoUpdate dto) {
        return Operation.builder()
                .id(dto.getId())
                .property(propertyService.getPropertyByCode(dto.getPropertyId()))
                .client(clientService.getClientByCedula(dto.getClientId()))
                .agent(agentService.getAgentByCedula(dto.getAgentId()))
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal())
                .operationType(dto.getOperationType())
                .value(dto.getValue())
                .commission(dto.getCommission())
                .processStatus(dto.getProcessStatus())
                .build();
    }
}