package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.NeighborhoodDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Neighborhood;
import co.edu.uniquindio.com.proptech.services.NeighborhoodService;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/neighborhoods")
public class NeighborhoodController {

    private final NeighborhoodService neighborhoodService;
    private final MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper;

    public NeighborhoodController(NeighborhoodService neighborhoodService, MapperCrud<Neighborhood, NeighborhoodDtoCreate, NeighborhoodDtoUpdate, NeighborhoodDtoReturn> neighborhoodMapper) {
        this.neighborhoodService = neighborhoodService;
        this.neighborhoodMapper = neighborhoodMapper;
    }

    @PostMapping
    public ResponseEntity<NeighborhoodDtoReturn> createNeighborhood(@Validated @RequestBody NeighborhoodDtoCreate neighborhoodDtoCreate) {
        Neighborhood neighborhood = neighborhoodMapper.toEntity(neighborhoodDtoCreate);
        Neighborhood saved = neighborhoodService.registerNeighborhood(neighborhood);
        return ResponseEntity.ok(neighborhoodMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeighborhoodDtoReturn> getNeighborhood(@PathVariable String id) {
        Neighborhood neighborhood = neighborhoodService.getNeighborhoodById(id);
        return ResponseEntity.ok(neighborhoodMapper.toDto(neighborhood));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NeighborhoodDtoReturn> updateNeighborhood(@PathVariable String id, @Validated @RequestBody NeighborhoodDtoUpdate neighborhoodDtoUpdate) {
        Neighborhood neighborhood = neighborhoodMapper.toUpdate(neighborhoodDtoUpdate);
        neighborhood.setId(id);
        Neighborhood updated = neighborhoodService.updateNeighborhood(neighborhood);
        return ResponseEntity.ok(neighborhoodMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNeighborhood(@PathVariable String id) {
        neighborhoodService.deleteNeighborhood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NeighborhoodDtoReturn>> getNeighborhoods() {
        ArrayList<Neighborhood> neighborhoods = neighborhoodService.getAllNeighborhoods();
        List<NeighborhoodDtoReturn> result = new java.util.ArrayList<>();
        for (Neighborhood neighborhood : neighborhoods) {
            result.add(neighborhoodMapper.toDto(neighborhood));
        }
        return ResponseEntity.ok(result);
    }
}
