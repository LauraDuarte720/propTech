package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.AbnormalAlertDto;
import co.edu.uniquindio.com.proptech.domain.model.AbnormalAlert;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.AbnormalAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abnormal-alerts")
public class AbnormalAlertController {

    private final AbnormalAlertService abnormalAlertService;
    private final MapperOnlyDto<AbnormalAlert, AbnormalAlertDto> abnormalAlertMapper;
    private final StructuresMappers structuresMappers;

    public AbnormalAlertController(AbnormalAlertService abnormalAlertService,
                                   MapperOnlyDto<AbnormalAlert, AbnormalAlertDto> abnormalAlertMapper,
                                   StructuresMappers structuresMappers) {
        this.abnormalAlertService = abnormalAlertService;
        this.abnormalAlertMapper = abnormalAlertMapper;
        this.structuresMappers = structuresMappers;
    }

    // Ejecutar todos los detectores manualmente
    @PostMapping("/run")
    public ResponseEntity<Void> runAllDetectors() {
        abnormalAlertService.runAllDetectors();
        return ResponseEntity.noContent().build();
    }

    // Obtener todas las alertas
    @GetMapping
    public ResponseEntity<List<AbnormalAlertDto>> getAllAlerts() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        abnormalAlertService.getAllAlerts(),
                        abnormalAlertMapper::toDto
                )
        );
    }

    // Obtener alertas por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AbnormalAlertDto>> getAlertsByType(
            @PathVariable String type) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        abnormalAlertService.getAlertsByType(type),
                        abnormalAlertMapper::toDto
                )
        );
    }

    // Obtener alertas por nivel de atención
    @GetMapping("/level/{level}")
    public ResponseEntity<List<AbnormalAlertDto>> getAlertsByLevel(
            @PathVariable String level) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        abnormalAlertService.getAlertsByLevel(level),
                        abnormalAlertMapper::toDto
                )
        );
    }

    // Marcar alerta como revisada
    @PatchMapping("/{id}/review")
    public ResponseEntity<Void> markAsReviewed(@PathVariable String id) {
        abnormalAlertService.markAsReviewed(id);
        return ResponseEntity.noContent().build();
    }
}