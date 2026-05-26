package co.edu.uniquindio.com.proptech.mappers.impl;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.stereotype.Component;

@Component
public class VisitMapper implements MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> {

    private final AgentService agentService;
    private final ClientService clientService;
    private final PropertyService propertyService;

    private final AgentMapper agentMapper;
    private final ClientMapper clientMapper;
    private final PropertyMapper propertyMapper;

    public VisitMapper(
            AgentService agentService,
            ClientService clientService,
            PropertyService propertyService,
            AgentMapper agentMapper,
            ClientMapper clientMapper,
            PropertyMapper propertyMapper
    ) {
        this.agentService = agentService;
        this.clientService = clientService;
        this.propertyService = propertyService;
        this.agentMapper = agentMapper;
        this.clientMapper = clientMapper;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public Visit toEntity(VisitDtoCreate dto) {

        Agent agent = dto.getAgentId() == null? null: agentService.getAgentByCedula(dto.getAgentId());

        Client client = clientService.getClientByCedula(dto.getClientId());

        Property property = propertyService.getPropertyByCode(dto.getPropertyId());

        return Visit.builder()
                .client(client)
                .property(property)
                .agent(agent)
                .date(dto.getDate())
                .visitType(dto.getVisitType())
                .postVisitNotes(dto.getPostVisitNotes())
                .build();
    }

    @Override
    public VisitDtoReturn toDto(Visit entity) {

        return VisitDtoReturn.builder()
                .id(entity.getId())
                .client(clientMapper.toDto(entity.getClient()))
                .property(propertyMapper.toDto(entity.getProperty()))
                .agent(agentMapper.toDto(entity.getAgent()))
                .date(entity.getDate())
                .status(entity.getStatus())
                .visitType(entity.getVisitType())
                .postVisitNotes(entity.getPostVisitNotes())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Override
    public Visit toUpdate(VisitDtoUpdate dto) {

        Visit.VisitBuilder builder = Visit.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .status(dto.getStatus())
                .visitType(dto.getVisitType())
                .postVisitNotes(dto.getPostVisitNotes());

        if (dto.getAgentId() != null) {
            builder.agent(agentService.getAgentByCedula(dto.getAgentId()));
        }

        if (dto.getClientId() != null) {
            builder.client(clientService.getClientByCedula(dto.getClientId()));
        }

        if (dto.getPropertyId() != null) {
            builder.property(propertyService.getPropertyByCode(dto.getPropertyId()));
        }

        return builder.build();
    }
}