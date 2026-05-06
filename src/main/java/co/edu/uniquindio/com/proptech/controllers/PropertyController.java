package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.services.PropertyService;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;

    public PropertyController(PropertyService propertyService, MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper,  MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper) {
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
        this.agentMapper = agentMapper;
    }

    @PostMapping
    public ResponseEntity<PropertyDtoReturn> createProperty(@Validated @RequestBody PropertyDtoCreate propertyDtoCreate, @Validated @RequestBody AgentDtoCreate agentDtoCreate) {
        Property property = propertyMapper.toEntity(propertyDtoCreate);
        Agent agent = agentMapper.toEntity(agentDtoCreate);
        Property saved = propertyService.registerProperty(property, agent);
        return ResponseEntity.ok(propertyMapper.toDto(saved));
    }

    @GetMapping("/{code}")
    public ResponseEntity<PropertyDtoReturn> getProperty(@PathVariable String code) {
        Property property = propertyService.getPropertyByCode(code);
        return ResponseEntity.ok(propertyMapper.toDto(property));
    }

    @PatchMapping("/{code}")
    public ResponseEntity<PropertyDtoReturn> updateProperty(@PathVariable String code, @Validated @RequestBody PropertyDtoUpdate propertyDtoUpdate) {
        Property property = propertyMapper.toUpdate(propertyDtoUpdate);
        property.setCode(code);
        Property updated = propertyService.updateProperty(property);
        return ResponseEntity.ok(propertyMapper.toDto(updated));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProperty(@PathVariable String code) {
        propertyService.deleteProperty(propertyService.getPropertyByCode(code));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PropertyDtoReturn>> getProperties() {
        HashTable<String, Property> properties = propertyService.getAllProperties();
        List<PropertyDtoReturn> result = new java.util.ArrayList<>();
        for (Property property : properties.values()) {
            result.add(propertyMapper.toDto(property));
        }
        return ResponseEntity.ok(result);
    }
}