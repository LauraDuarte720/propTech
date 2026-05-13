package co.edu.uniquindio.com.proptech.config.mappers.impl;

import co.edu.uniquindio.com.proptech.config.mappers.MapperOnlyEntity;
import co.edu.uniquindio.com.proptech.domain.dtos.CreateSupportRequestDto;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.domain.model.SupportRequest;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.ClientService;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.stereotype.Component;



@Component
public class SupportRequestMapper  {

    ClientService clientService;
    AgentService agentService;
    PropertyService propertyService;

    public SupportRequestMapper(ClientService clientService, AgentService agentService, PropertyService propertyService) {
        this.clientService = clientService;
        this.agentService = agentService;
        this.propertyService = propertyService;
    }

    public SupportRequest toEntity(CreateSupportRequestDto dto) {
        return SupportRequest.builder()
                .client(clientService.getClientByCedula(dto.getClientId()))
                .property(propertyService.getPropertyByCode(dto.getPropertyId()))
                .agent(agentService.getAgentByCedula(dto.getAgentId()))
                .message(dto.getMessage())
                .build();
    }
}