package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.AdminActionLogDtoReturn;
import co.edu.uniquindio.com.proptech.mappers.impl.AdminActionLogMapper;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.AdminActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-actions")
public class AdminActionController {

    private final AdminActionService adminActionService;
    private final AdminActionLogMapper mapper;
    private final StructuresMappers structuresMappers;

    public AdminActionController(AdminActionService adminActionService,
                                 AdminActionLogMapper mapper,
                                 StructuresMappers structuresMappers) {
        this.adminActionService = adminActionService;
        this.mapper = mapper;
        this.structuresMappers = structuresMappers;
    }

    @GetMapping("/peek")
    public ResponseEntity<AdminActionLogDtoReturn> peekLastAction() {
        return ResponseEntity.ok(mapper.toDto(adminActionService.peekLastAction()));
    }

    @PostMapping("/undo")
    public ResponseEntity<Void> undoLastAction() {
        adminActionService.undoLastAction();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<AdminActionLogDtoReturn>> getHistory() {
        return ResponseEntity.ok(
                structuresMappers.fromQueue(adminActionService.getHistory(), mapper)
        );
    }
}
