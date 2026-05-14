package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.config.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoCreate;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoUpdate;
import co.edu.uniquindio.com.proptech.domain.dtos.VisitDtoReturn;
import co.edu.uniquindio.com.proptech.domain.model.Visit;
import co.edu.uniquindio.com.proptech.services.VisitService;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;
    private final MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper;

    public VisitController(VisitService visitService, MapperCrud<Visit, VisitDtoCreate, VisitDtoUpdate, VisitDtoReturn> visitMapper) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
    }

    @PostMapping
    public ResponseEntity<VisitDtoReturn> createVisit(@Validated @RequestBody VisitDtoCreate visitDtoCreate) {
        Visit visit = visitMapper.toEntity(visitDtoCreate);
        Visit saved = visitService.registerVisit(visit);
        return ResponseEntity.ok(visitMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDtoReturn> getVisit(@PathVariable String id) {
        Visit visit = visitService.getVisitById(id);
        return ResponseEntity.ok(visitMapper.toDto(visit));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VisitDtoReturn> updateVisit(@PathVariable String id, @Validated @RequestBody VisitDtoUpdate visitDtoUpdate) {
        Visit visit = visitMapper.toUpdate(visitDtoUpdate);
        visit.setId(id);
        Visit updated = visitService.updateVisit(visit);
        return ResponseEntity.ok(visitMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable String id) {
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<VisitDtoReturn>> getVisits() {
        LinkedList<Visit> visits = visitService.getAllVisits();
        List<VisitDtoReturn> result = new java.util.ArrayList<>();
        for (int i = 0; i < visits.size(); i++) {
            result.add(visitMapper.toDto(visits.get(i)));
        }
        return ResponseEntity.ok(result);
    }
}