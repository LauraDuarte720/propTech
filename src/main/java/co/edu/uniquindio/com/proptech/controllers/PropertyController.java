package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.enums.City;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyStatus;
import co.edu.uniquindio.com.proptech.domain.enums.PropertyType;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final StructuresMappers structuresMappers;

    public PropertyController(PropertyService propertyService,
                              MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper,
                              StructuresMappers structuresMappers) {
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
        this.structuresMappers = structuresMappers;
    }

    // ══════════════════════════════════════════════
    // CRUD BÁSICO
    // ══════════════════════════════════════════════

    @PostMapping
    public ResponseEntity<PropertyDtoReturn> createProperty(
            @RequestParam(required = false) String agentId,
            @RequestParam(defaultValue = "false") boolean confirm,
            @Validated @RequestBody PropertyDtoCreate dto) {
        Property saved = propertyService.registerProperty(propertyMapper.toEntity(dto), agentId, confirm);
        return ResponseEntity.ok(propertyMapper.toDto(saved));
    }

    @PostMapping("/with-agent/{agentId}")
    public ResponseEntity<PropertyDtoReturn> createAndPublishProperty(
            @PathVariable String agentId,
            @Validated @RequestBody PropertyDtoCreate dto) {
        Property saved = propertyService.registerAndPublishProperty(propertyMapper.toEntity(dto), agentId);
        return ResponseEntity.ok(propertyMapper.toDto(saved));
    }

    @GetMapping("/{code}")
    public ResponseEntity<PropertyDtoReturn> getProperty(@PathVariable String code) {
        return ResponseEntity.ok(propertyMapper.toDto(propertyService.getPropertyByCode(code)));
    }

    @GetMapping
    public ResponseEntity<List<PropertyDtoReturn>> getAllProperties() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableValues(
                        propertyService.getAllProperties(),
                        propertyMapper::toDto
                )
        );
    }

    @PatchMapping("/{code}")
    public ResponseEntity<PropertyDtoReturn> updateProperty(
            @PathVariable String code,
            @RequestParam(defaultValue = "false") boolean confirm,
            @Validated @RequestBody PropertyDtoUpdate dto) {
        Property property = propertyMapper.toUpdate(dto);
        property.setCode(code);
        return ResponseEntity.ok(propertyMapper.toDto(propertyService.updateProperty(property, confirm)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProperty(@PathVariable String code) {
        propertyService.deleteProperty(code);
        return ResponseEntity.noContent().build();
    }

    // ══════════════════════════════════════════════
    // PUBLICACIÓN
    // ══════════════════════════════════════════════

    @PatchMapping("/{code}/publish")
    public ResponseEntity<PropertyDtoReturn> publishProperty(@PathVariable String code) {
        return ResponseEntity.ok(propertyMapper.toDto(propertyService.publishProperty(code)));
    }

    @PatchMapping("/{code}/unpublish")
    public ResponseEntity<PropertyDtoReturn> unpublishProperty(@PathVariable String code) {
        return ResponseEntity.ok(propertyMapper.toDto(propertyService.unpublishProperty(code)));
    }

    // ══════════════════════════════════════════════
    // HISTORIAL Y SNAPSHOT
    // ══════════════════════════════════════════════

    @PostMapping("/{code}/undo")
    public ResponseEntity<PropertyDtoReturn> undoLastChange(@PathVariable String code) {
        return ResponseEntity.ok(propertyMapper.toDto(propertyService.undoLastChange(code)));
    }

    // ══════════════════════════════════════════════
    // ORDENAMIENTO
    // ══════════════════════════════════════════════

    @GetMapping("/ordered/price")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesOrderedByPrice() {
        return ResponseEntity.ok(
                structuresMappers.fromAVLTree(
                        propertyService.getPropertiesOrderedByPrice(),
                        propertyMapper::toDto
                )
        );
    }

    @GetMapping("/ordered/area")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesOrderedByArea() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        propertyService.getPropertiesOrderedByArea(),
                        propertyMapper::toDto
                )
        );
    }

    @GetMapping("/ordered/demand")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesOrderedByDemand() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        propertyService.getPropertiesOrderedByDemand(),
                        propertyMapper::toDto
                )
        );
    }

    // ══════════════════════════════════════════════
    // FILTROS
    // ══════════════════════════════════════════════

    @GetMapping("/price-range")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        propertyService.getPropertiesByPriceRange(min, max),
                        propertyMapper::toDto
                )
        );
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesByCity(
            @PathVariable City city) {
        var byCity = propertyService.getPropertiesByCity();
        var props = byCity.get(city);
        if (props == null) return ResponseEntity.ok(java.util.List.of());
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(props, propertyMapper::toDto)
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesByType(
            @PathVariable PropertyType type) {
        var byType = propertyService.getPropertiesByType();
        var props = byType.get(type);
        if (props == null) return ResponseEntity.ok(java.util.List.of());
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(props, propertyMapper::toDto)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PropertyDtoReturn>> getPropertiesByStatus(
            @PathVariable PropertyStatus status) {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(propertyService.getPropertiesByStatus(status), propertyMapper::toDto)
        );
    }
}