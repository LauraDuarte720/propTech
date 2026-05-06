package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoReturn;
import co.edu.uniquindio.com.proptech.domain.dtos.AgentDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/agents")
public class AgentController {


    private final AgentService agentService;
    private final MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;

    public AgentController(AgentService agentService, MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper) {
        this.agentService = agentService;
        this.agentMapper = agentMapper;
    }

    @PostMapping
    public ResponseEntity<AgentDtoReturn> createAgent(@Validated @RequestBody AgentDtoCreate agentDtoCreate) {
        Agent agent = agentMapper.toEntity(agentDtoCreate);
        Agent agentSaved = agentService.registerAgent(agent);
        return ResponseEntity.ok(agentMapper.toDto(agentSaved));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<AgentDtoReturn> getAgent(@PathVariable String cedula) {
        Agent agent = agentService.getAgentByCedula(cedula);
        return ResponseEntity.ok(agentMapper.toDto(agent));
    }

    @PatchMapping("/{cedula}")
    public ResponseEntity<AgentDtoReturn> updateAgent(@PathVariable String cedula, @Validated @RequestBody AgentDtoUpdate agentDtoUpdate) {
        Agent agent = agentMapper.toUpdate(agentDtoUpdate);
        agent.setCedula(cedula);
        Agent agentSaved = agentService.updateAgent(agent);
        return ResponseEntity.ok(agentMapper.toDto(agentSaved));
    }

    @GetMapping
    public ResponseEntity<List<AgentDtoReturn>> getAllAgents() {
        HashTable<String,Agent> hashTable = agentService.getAgents();
        List<AgentDtoReturn> resul = new ArrayList<>();
        for (Agent agent : hashTable.values()) {
            resul.add(agentMapper.toDto(agent));
        }
        return ResponseEntity.ok(resul);
    }
}
