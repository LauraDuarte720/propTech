package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.*;
import co.edu.uniquindio.com.proptech.domain.model.Agent;
import co.edu.uniquindio.com.proptech.domain.model.Property;
import co.edu.uniquindio.com.proptech.mappers.MapperCrud;
import co.edu.uniquindio.com.proptech.mappers.structuresMappers.StructuresMappers;
import co.edu.uniquindio.com.proptech.services.ReportService;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper;
    private final MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper;
    private final StructuresMappers structuresMappers;

    public ReportController(ReportService reportService,
                            MapperCrud<Property, PropertyDtoCreate, PropertyDtoUpdate, PropertyDtoReturn> propertyMapper,
                            MapperCrud<Agent, AgentDtoCreate, AgentDtoUpdate, AgentDtoReturn> agentMapper,
                            StructuresMappers structuresMappers) {
        this.reportService = reportService;
        this.propertyMapper = propertyMapper;
        this.agentMapper = agentMapper;
        this.structuresMappers = structuresMappers;
    }

    @GetMapping("/zone")
    public ResponseEntity<List<ZoneReportDto>> getReportByZone() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToList(
                        reportService.getReportByZone(),
                        (city, zr) -> ZoneReportDto.builder()
                                .city(city)
                                .totalProperties(zr.totalProperties)
                                .totalVisits(zr.totalVisits)
                                .totalClosures(zr.totalClosures)
                                .build()
                )
        );
    }

    @GetMapping("/price")
    public ResponseEntity<List<PriceReportDto>> getReportByPrice() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToList(
                        reportService.getReportByPriceRange(),
                        (range, props) -> PriceReportDto.builder()
                                .range(range)
                                .totalProperties(props.size())
                                .properties(structuresMappers.fromArrayList(props, propertyMapper::toDto))
                                .build()
                )
        );
    }

    @GetMapping("/visits")
    public ResponseEntity<List<VisitReportDto>> getReportByVisits() {
        return ResponseEntity.ok(
                structuresMappers.fromArrayList(
                        reportService.getReportByVisits(),
                        vr -> VisitReportDto.builder()
                                .property(propertyMapper.toDto(vr.property))
                                .totalVisits(vr.totalVisits)
                                .build()
                )
        );
    }

    @GetMapping("/closures")
    public ResponseEntity<List<ClosureReportDto>> getReportByClosures() {
        return ResponseEntity.ok(
                structuresMappers.fromHashTableToList(
                        reportService.getReportByClosures(),
                        (agentId, cr) -> ClosureReportDto.builder()
                                .agent(agentMapper.toDto(cr.agent))
                                .totalClosures(cr.totalClosures)
                                .totalValue(cr.totalValue)
                                .totalCommission(cr.totalCommission)
                                .build()
                )
        );
    }
}