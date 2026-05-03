package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper implements MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> {

    MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;
    MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;

    public OperationMapper(MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper, MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper, MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper) {
        this.agentMapper = agentMapper;
        this.clientMapper = clientMapper;
        this.propertyMapper = propertyMapper;
    }
    @Override
    public Operation toEntity(OperationDtoCreate dto) {
        return Operation.builder()
                .property(null)
                .client(null)
                .agent(null)
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal())
                .operationType(dto.getOperationType())
                .value(dto.getValue())
                .commission(dto.getCommission())
                .processStatus(dto.getProcessStatus())
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
                .property(null)
                .client(null)
                .agent(null)
                .dateInitial(dto.getDateInitial())
                .dateFinal(dto.getDateFinal())
                .operationType(dto.getOperationType())
                .value(dto.getValue())
                .commission(dto.getCommission())
                .processStatus(dto.getProcessStatus())
                .build();
    }
}