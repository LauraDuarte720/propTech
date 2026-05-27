package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.BasicAlertDto;
import co.edu.uniquindio.com.proptech.domain.model.BasicAlert;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.BasicAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basic-alerts")
public class BasicAlertController {

    private final BasicAlertService basicAlertService;
    private final MapperOnlyDto<BasicAlert, BasicAlertDto> basicAlertMapper;
    private final StructuresMappers structuresMappers;

    public BasicAlertController(BasicAlertService basicAlertService,
                                MapperOnlyDto<BasicAlert, BasicAlertDto> basicAlertMapper, StructuresMappers structuresMappers) {
        this.basicAlertService = basicAlertService;
        this.basicAlertMapper = basicAlertMapper;
        this.structuresMappers = structuresMappers;
    }

    // Generar todas las alertas
    @PostMapping("/generate")
    public ResponseEntity<Void> generateAllAlerts() {
        basicAlertService.generateAllAlerts();
        return ResponseEntity.noContent().build();
    }

    // Obtener y marcar como revisada la siguiente alerta urgente (CONTRACT_EXPIRING)
    @PostMapping("/priority/next")
    public ResponseEntity<BasicAlertDto> getNextPriorityAlert(@RequestParam String agentCedula) {
        return ResponseEntity.ok(
                basicAlertMapper.toDto(basicAlertService.getNextPriorityAlert(agentCedula))
        );
    }

    // Obtener y marcar como revisada la siguiente alerta pendiente
    @PostMapping("/pending/next")
    public ResponseEntity<BasicAlertDto> getNextPendingAlert(@RequestParam String agentCedula) {
        BasicAlert alert =  basicAlertService.getNextPendingAlert(agentCedula);
        return ResponseEntity.ok(basicAlertMapper.toDto(alert));
    }

    // Generar alertas específicas individualmente
    @PostMapping("/generate/contract-expiring")
    public ResponseEntity<Void> generateContractExpiring() {
        basicAlertService.createAlertContractExpiring();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/property-no-visits")
    public ResponseEntity<Void> generatePropertyNoVisits() {
        basicAlertService.createAlertPropertyNoVisits();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/high-demand")
    public ResponseEntity<Void> generateHighDemand() {
        basicAlertService.createAlertHighDemand();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/pending-visit-confirmation")
    public ResponseEntity<Void> generatePendingVisitConfirmation() {
        basicAlertService.createAlertPendingVisitConfirmation();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/reserve-no-closure")
    public ResponseEntity<Void> generateReserveNoClosure() {
        basicAlertService.createAlertReserveNoClosure();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate/inactive-client")
    public ResponseEntity<Void> generateInactiveClient() {
        basicAlertService.createAlertInactiveClient();
        return ResponseEntity.noContent().build();
    }

    // Todas las alertas guardadas (para mostrar en lista/cards)
    @GetMapping("/all")
    public ResponseEntity<List<BasicAlertDto>> getAllAlerts() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        basicAlertService.getAllAlerts(),
                        basicAlertMapper::toDto
                )
        );
    }

    // Ver la siguiente urgente sin sacarla de la cola
    @GetMapping("/priority/peek")
    public ResponseEntity<BasicAlertDto> peekPriorityAlert(@RequestParam String agentCedula) {
        return ResponseEntity.ok(
                basicAlertMapper.toDto(basicAlertService.peekPriorityAlert(agentCedula))
        );
    }
}