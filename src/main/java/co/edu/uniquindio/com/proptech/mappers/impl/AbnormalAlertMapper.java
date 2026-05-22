package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class AbnormalAlertMapper implements MapperOnlyDto<AbnormalAlert, AbnormalAlertDto> {

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
    public AbnormalAlertDto toDto(AbnormalAlert entity) {
        return AbnormalAlertDto.builder()
                .id(entity.getId())
                .timestamp(entity.getTimestamp())
                .reviewed(entity.isReviewed())
                .alertAbnormalType(entity.getAlertAbnormalType())
                .attentionLevel(entity.getAttentionLevel())
                .agent(entity.getAgent() != null ? agentMapper.toDto(entity.getAgent()) : null)
                .client(entity.getClient() != null ? clientMapper.toDto(entity.getClient()) : null)
                .operation(entity.getOperation() != null ? operationMapper.toDto(entity.getOperation()) : null)
                .property(entity.getProperty() != null ? propertyMapper.toDto(entity.getProperty()) : null)
                .visit(entity.getVisit() != null ? visitMapper.toDto(entity.getVisit()) : null)
                .build();
    }
}
