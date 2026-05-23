package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.MapperCreate;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;
    private final MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    private final MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper;
    private final MapperCreate<SupportRequest, SupportRequestDtoCreate, SupportRequestDtoReturn> supportRequestMapper;
    private final StructuresMappers structuresMappers;

    public AgentController(AgentService agentService,
                           MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper,
                           MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper,
                           MapperCreate<SupportRequest, SupportRequestDtoCreate, SupportRequestDtoReturn> supportRequestMapper,
                           StructuresMappers structuresMappers) {
        this.agentService = agentService;
        this.agentMapper = agentMapper;
        this.visitMapper = visitMapper;
        this.supportRequestMapper = supportRequestMapper;
        this.structuresMappers = structuresMappers;
    }

    // ══════════════════════════════════════════════
    // CRUD BÁSICO
    // ══════════════════════════════════════════════

    @PostMapping
    public ResponseEntity<AgentDtoReturn> createAgent(
            @Validated @RequestBody AgentDtoCreate dto) {
        Agent saved = agentService.registerAgent(agentMapper.toEntity(dto));
        return ResponseEntity.ok(agentMapper.toDto(saved));
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<AgentDtoReturn> getAgent(@PathVariable String cedula) {
        return ResponseEntity.ok(agentMapper.toDto(agentService.getAgentByCedula(cedula)));
    }

    @GetMapping
    public ResponseEntity<List<AgentDtoReturn>> getAllAgents() {
        HashTable<String, Agent> agents = agentService.getAgents();
        return ResponseEntity.ok(structuresMappers.fromHashTableValues(agents, agentMapper::toDto));
    }

    @PatchMapping("/{cedula}")
    public ResponseEntity<AgentDtoReturn> updateAgent(
            @PathVariable String cedula,
            @RequestParam(defaultValue = "false") boolean confirm,
            @Validated @RequestBody AgentDtoUpdate dto) {
        Agent agent = agentMapper.toUpdate(dto);
        agent.setCedula(cedula);
        return ResponseEntity.ok(agentMapper.toDto(agentService.updateAgent(agent, confirm)));
    }

    // ══════════════════════════════════════════════
    // VISITAS
    // ══════════════════════════════════════════════

    @GetMapping("/{cedula}/visits")
    public ResponseEntity<List<VisitDtoReturn>> getAgentVisits(@PathVariable String cedula) {
        return ResponseEntity.ok(
                structuresMappers.fromPriorityQueue(
                        agentService.getVisitsAgent(cedula),
                        visitMapper::toDto
                )
        );
    }

    @PostMapping("/{cedula}/visits")
    public ResponseEntity<VisitDtoReturn> registerVisit(
            @PathVariable String cedula,
            @Validated @RequestBody VisitDtoCreate dto) {
        Visit visit = visitMapper.toEntity(dto);
        visit.setAgent(agentService.getAgentByCedula(cedula));
        return ResponseEntity.ok(visitMapper.toDto(agentService.registerVisit(visit)));
    }

    // ══════════════════════════════════════════════
    // PROPIEDADES
    // ══════════════════════════════════════════════

    @PostMapping("/{cedula}/properties/{propertyCode}")
    public ResponseEntity<Void> assignProperty(
            @PathVariable String cedula,
            @PathVariable String propertyCode) {
        agentService.addPropertyToAgent(propertyCode, cedula);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cedula}/properties/{propertyCode}")
    public ResponseEntity<Void> removeProperty(
            @PathVariable String cedula,
            @PathVariable String propertyCode) {
        agentService.removePropertyFromAgent(propertyCode, cedula);
        return ResponseEntity.noContent().build();
    }

    // ══════════════════════════════════════════════
    // SUPPORT REQUESTS
    // ══════════════════════════════════════════════

    @PostMapping("/{cedula}/support-requests")
    public ResponseEntity<SupportRequestDtoReturn> registerSupportRequest(
            @PathVariable String cedula,
            @Validated @RequestBody SupportRequestDtoCreate dto) {
        SupportRequest request = supportRequestMapper.toEntity(dto);
        request.setAgent(agentService.getAgentByCedula(cedula));
        return ResponseEntity.ok(
                supportRequestMapper.toDto(agentService.registerSupportRequest(request))
        );
    }

    @GetMapping("/{cedula}/support-requests/next")
    public ResponseEntity<SupportRequestDtoReturn> getNextSupportRequest(
            @PathVariable String cedula) {
        return ResponseEntity.ok(
                supportRequestMapper.toDto(agentService.getNextSupportRequest(cedula))
        );
    }

    @PostMapping("/{cedula}/support-requests/attend")
    public ResponseEntity<SupportRequestDtoReturn> attendSupportRequest(
            @PathVariable String cedula) {
        return ResponseEntity.ok(
                supportRequestMapper.toDto(agentService.attendSupportRequest(cedula))
        );
    }

    @PatchMapping("/{cedula}/support-requests/{requestId}/cancel")
    public ResponseEntity<Void> cancelSupportRequest(
            @PathVariable String cedula,
            @PathVariable String requestId) {
        agentService.cancelSupportRequest(cedula, requestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cedula}/support-requests/history")
    public ResponseEntity<List<SupportRequestDtoReturn>> getSupportHistory(
            @PathVariable String cedula) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        agentService.getSupportHistory(cedula),
                        supportRequestMapper::toDto
                )
        );
    }

    // ══════════════════════════════════════════════
    // REPORTES Y ORDENAMIENTO
    // ══════════════════════════════════════════════

    @GetMapping("/ordered-by-closed-deals")
    public ResponseEntity<List<AgentDtoReturn>> getAgentsOrderedByClosedDeals() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        agentService.getAgentsOrderedByClosedDeals(),
                        agentMapper::toDto
                )
        );
    }
}