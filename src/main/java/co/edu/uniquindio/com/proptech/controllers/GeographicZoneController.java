package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.GeographicZone;
import co.edu.uniquindio.com.proptech.services.GeographicZoneService;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geo-zones")
public class GeographicZoneController {

    private final GeographicZoneService geographicZoneService;
    private final MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper;

    public GeographicZoneController(GeographicZoneService geographicZoneService, MapperCrud<GeographicZone, GeographicZoneDtoCreate, GeographicZoneDtoUpdate, GeographicZoneDtoReturn> geographicZoneMapper) {
        this.geographicZoneService = geographicZoneService;
        this.geographicZoneMapper = geographicZoneMapper;
    }

    @PostMapping
    public ResponseEntity<GeographicZoneDtoReturn> createGeographicZone(@Validated @RequestBody GeographicZoneDtoCreate geographicZoneDtoCreate) {
        GeographicZone geographicZone= geographicZoneMapper.toEntity(geographicZoneDtoCreate);
        GeographicZone saved = geographicZoneService.registerGeographicZone(geographicZone);
        return ResponseEntity.ok(geographicZoneMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeographicZoneDtoReturn> getClient(@PathVariable String id){
        GeographicZone geographicZone = geographicZoneService.getGeographicZoneById(id);
        return ResponseEntity.ok(geographicZoneMapper.toDto(geographicZone));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GeographicZoneDtoReturn> updateClient(@PathVariable String id, @Validated @RequestBody GeographicZoneDtoUpdate geographicZoneDtoUpdate) {
        GeographicZone geographicZone = geographicZoneMapper.toUpdate(geographicZoneDtoUpdate);
        geographicZone.setId(id);
        GeographicZone updated = geographicZoneService.updateGeographicZone(geographicZone);
        return ResponseEntity.ok(geographicZoneMapper.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<GeographicZoneDtoReturn>> getClients() {
        ArrayList<GeographicZone> geographicZones = geographicZoneService.getAllGeographicZones();
        List<GeographicZoneDtoReturn> result = new java.util.ArrayList<>();
        for (GeographicZone geographicZone : geographicZones) {
            result.add(geographicZoneMapper.toDto(geographicZone));
        }
        return ResponseEntity.ok(result);
    }
}
