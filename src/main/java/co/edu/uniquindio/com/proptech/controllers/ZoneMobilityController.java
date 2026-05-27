package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.ZoneTransitionPatternDto;
import co.edu.uniquindio.com.proptech.domain.model.ZoneTransitionPattern;
import co.edu.uniquindio.com.proptech.mappers.MapperOnlyDto;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.ZoneMobilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zone-mobility")
public class ZoneMobilityController {

    private final ZoneMobilityService zoneMobilityService;
    private final MapperOnlyDto<ZoneTransitionPattern, ZoneTransitionPatternDto> patternMapper;
    private final StructuresMappers structuresMappers;

    public ZoneMobilityController(ZoneMobilityService zoneMobilityService,
                                  MapperOnlyDto<ZoneTransitionPattern, ZoneTransitionPatternDto> patternMapper,
                                  StructuresMappers structuresMappers) {
        this.zoneMobilityService = zoneMobilityService;
        this.patternMapper = patternMapper;
        this.structuresMappers = structuresMappers;
    }

    // Todos los patrones de movilidad
    @GetMapping
    public ResponseEntity<List<ZoneTransitionPatternDto>> getAllPatterns() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getAllMobilityPatterns(),
                        patternMapper::toDto
                )
        );
    }

    // Patrones salientes desde una zona
    @GetMapping("/from/{zoneKey}")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getPatternsFrom(
            @PathVariable String zoneKey) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getMobilityPatternsFrom(zoneKey),
                        patternMapper::toDto
                )
        );
    }

    // Patrones entrantes hacia una zona
    @GetMapping("/to/{zoneKey}")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getPatternsTo(
            @PathVariable String zoneKey) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getMobilityPatternsTo(zoneKey),
                        patternMapper::toDto
                )
        );
    }

    // Zonas destino más populares
    @GetMapping("/top-destinations")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getTopDestinations() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getTopDestinationZones(),
                        patternMapper::toDto
                )
        );
    }

    // Patrones correlacionados con operaciones cerradas
    @GetMapping("/correlated")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getCorrelatedPatterns() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getOperationCorrelatedPatterns(),
                        patternMapper::toDto
                )
        );
    }

    @GetMapping("/by-level")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getAllPatternsByLevel(
            @RequestParam(defaultValue = "ZONE") String level) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getAllMobilityPatternsByLevel(level),
                        patternMapper::toDto));
    }

    @GetMapping("/correlated/by-level")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getCorrelatedByLevel(
            @RequestParam(defaultValue = "ZONE") String level) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getOperationCorrelatedPatternsByLevel(level),
                        patternMapper::toDto));
    }

    @GetMapping("/top-destinations/by-level")
    public ResponseEntity<List<ZoneTransitionPatternDto>> getTopDestinationsByLevel(
            @RequestParam(defaultValue = "ZONE") String level) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        zoneMobilityService.getTopDestinationZonesByLevel(level),
                        patternMapper::toDto));
    }
}