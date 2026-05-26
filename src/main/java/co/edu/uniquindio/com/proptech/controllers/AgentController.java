package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.MapperCreate;
import co.edu.uniquindio.com.proptech.mappers.impl.ClientMapper;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.AgentService;
import co.edu.uniquindio.com.proptech.services.VisitService;
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
    private final MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final VisitService  visitService;
    private final ClientMapper clientMapper;


    public AgentController(AgentService agentService,
                           MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper,
                           MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper,
                           MapperCreate<SupportRequest, SupportRequestDtoCreate, SupportRequestDtoReturn> supportRequestMapper,
                           StructuresMappers structuresMappers, MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper, MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper, VisitService visitService, ClientMapper clientMapper) {
        this.agentService = agentService;
        this.agentMapper = agentMapper;
        this.visitMapper = visitMapper;
        this.supportRequestMapper = supportRequestMapper;
        this.structuresMappers = structuresMappers;
        this.geographicZoneMapper = geographicZoneMapper;
        this.propertyMapper = propertyMapper;
        this.visitService = visitService;
        this.clientMapper = clientMapper;
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
    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> deleteAgent(@PathVariable String cedula) {
        agentService.deleteAgent(cedula);
        return ResponseEntity.noContent().build();
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
        dto.setAgentId(cedula); // toma la cédula del path, ignora lo que mande el body
        Visit visit = visitMapper.toEntity(dto);
        Agent agent = agentService.getAgentByCedula(cedula);
        return ResponseEntity.ok(visitMapper.toDto(agentService.registerVisit(agent, visit)));
    }

    @PostMapping("/{cedula}/visits/attend")
    public ResponseEntity<VisitDtoReturn> attendVisit(@PathVariable String cedula) {
        return ResponseEntity.ok(visitMapper.toDto(agentService.attendVisit(cedula)));
    }

    @GetMapping("/{cedula}/visits/history")
    public ResponseEntity<List<VisitDtoReturn>> getAgentVisitHistory(@PathVariable String cedula) {
        return ResponseEntity.ok(
                structuresMappers.fromLinkedList(
                        visitService.getAllAgentVisitHistory(cedula),
                        visitMapper::toDto
                )
        );
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

    @GetMapping("/{agentId}/support-requests/count")
    public ResponseEntity<Integer> getPendingRequestsCount(
            @PathVariable String agentId) {

        return ResponseEntity.ok(
                agentService.getPendingSupportRequestsCount(agentId)
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

    @GetMapping("/{cedula}/properties")
    public ResponseEntity<List<PropertyDtoReturn>> getAgentProperties(@PathVariable String cedula) {
        Agent agent = agentService.getAgentByCedula(cedula);
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(agent.getAssignedProperties(), propertyMapper::toDto)
        );
    }

    @PatchMapping("/{cedula}/zone")
    public ResponseEntity<AgentDtoReturn> updateAgentZone(
            @PathVariable String cedula,
            @RequestParam(defaultValue = "false") boolean confirm,
            @Validated @RequestBody GeographicZoneDtoCreate dto) {
        GeographicZone zone = geographicZoneMapper.toEntity(dto);
        Agent updated = agentService.updateAgentZone(cedula, zone, confirm);
        return ResponseEntity.ok(agentMapper.toDto(updated));
    }

    @GetMapping("/{cedula}/potential-clients")
    public ResponseEntity<List<ClientDtoReturn>> getPotentialClientsForAgent(
            @PathVariable String cedula) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        agentService.getPotentialClientsForAgent(cedula),
                        clientMapper::toDto
                )
        );
    }
}