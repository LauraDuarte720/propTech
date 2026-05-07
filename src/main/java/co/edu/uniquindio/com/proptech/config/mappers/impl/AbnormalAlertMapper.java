package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.config.mappers.MapperSimple;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class AbnormalAlertMapper implements MapperSimple<AbnormalAlert, AbnormalAlertDto> {

    MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper;
    MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper;
    MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper;

    public AbnormalAlertMapper(MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper, MapperCrud<Client, ClientDtoCreate, ClientDtoUpdate, ClientDtoReturn> clientMapper, MapperCrud<Operation, OperationDtoCreate, OperationDtoUpdate, OperationDtoReturn> operationMapper, MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper, MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper) {
        this.agentMapper = agentMapper;
        this.clientMapper = clientMapper;
        this.operationMapper = operationMapper;
        this.propertyMapper = propertyMapper;
        this.visitMapper = visitMapper;
    }

    @Override
    public AbnormalAlertDto toOnlyDto(AbnormalAlert entity) {
        return AbnormalAlertDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .reviewed(entity.isReviewed())
                .alertAbnormalType(entity.getAlertAbnormalType())
                .attentionLevel(entity.getAttentionLevel())
                .agent(agentMapper.toDto(entity.getAgent()))
                .client(clientMapper.toDto(entity.getClient()))
                .operation(operationMapper.toDto(entity.getOperation()))
                .property(propertyMapper.toDto(entity.getProperty()))
                .visit(visitMapper.toDto(entity.getVisit()))
                .build();
    }
}
