package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.dtos.LoginRequestDto;
import co.edu.uniquindio.com.proptech.domain.dtos.LoginResponseDto;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Client;
import co.edu.uniquindio.com.proptech.exceptions.specificExceptions.InvalidCredentialsException;
import co.edu.uniquindio.com.proptech.repositories.AgentRepository;
import co.edu.uniquindio.com.proptech.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;

    public AuthService(AgentRepository agentRepository, ClientRepository clientRepository) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
    }

    public LoginResponseDto login(LoginRequestDto request) {

        // Verificar admin
        if (request.getUsername().equals(adminUsername) &&
                request.getPassword().equals(adminPassword)) {
            return LoginResponseDto.builder()
                    .cedula("0")
                    .name(adminName)
                    .username(adminUsername)
                    .role("ADMIN")
                    .build();
        }

        // Verificar agente
        for (Agent agent : agentRepository.getAgents().values()) {
            if (agent.getUsername().equals(request.getUsername()) &&
                    agent.getPassword().equals(request.getPassword())) {
                return LoginResponseDto.builder()
                        .cedula(agent.getCedula())
                        .name(agent.getName())
                        .username(agent.getUsername())
                        .role("AGENT")
                        .build();
            }
        }

        // Verificar cliente
        for (Client client : clientRepository.getClients().values()) {
            if (client.getUsername().equals(request.getUsername()) &&
                    client.getPassword().equals(request.getPassword())) {
                return LoginResponseDto.builder()
                        .cedula(client.getCedula())
                        .name(client.getName())
                        .username(client.getUsername())
                        .role("CLIENT")
                        .build();
            }
        }

        throw new InvalidCredentialsException();
    }
}