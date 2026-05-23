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

    // Reporte por zona
    @GetMapping("/zone")
    public ResponseEntity<List<ZoneReportDto>> getReportByZone() {
        HashTable<String, ReportService.ZoneReport> report = reportService.getReportByZone();
        List<ZoneReportDto> result = new java.util.ArrayList<>();
        for (String city : report.keys()) {
            ReportService.ZoneReport zr = report.get(city);
            result.add(ZoneReportDto.builder()
                    .city(city)
                    .totalProperties(zr.totalProperties)
                    .totalVisits(zr.totalVisits)
                    .totalClosures(zr.totalClosures)
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    // Reporte por precio
    @GetMapping("/price")
    public ResponseEntity<List<PriceReportDto>> getReportByPrice() {
        HashTable<String, ArrayList<Property>> report = reportService.getReportByPriceRange();
        List<PriceReportDto> result = new java.util.ArrayList<>();
        for (String range : report.keys()) {
            ArrayList<Property> props = report.get(range);
            result.add(PriceReportDto.builder()
                    .range(range)
                    .totalProperties(props.size())
                    .properties(structuresMappers.fromArrayList(props, propertyMapper::toDto))
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    // Reporte por visitas
    @GetMapping("/visits")
    public ResponseEntity<List<VisitReportDto>> getReportByVisits() {
        ArrayList<ReportService.VisitReport> report = reportService.getReportByVisits();
        List<VisitReportDto> result = new java.util.ArrayList<>();
        for (int i = 0; i < report.size(); i++) {
            ReportService.VisitReport vr = report.get(i);
            result.add(VisitReportDto.builder()
                    .property(propertyMapper.toDto(vr.property))
                    .totalVisits(vr.totalVisits)
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    // Reporte por cierres
    @GetMapping("/closures")
    public ResponseEntity<List<ClosureReportDto>> getReportByClosures() {
        HashTable<String, ReportService.ClosureReport> report = reportService.getReportByClosures();
        List<ClosureReportDto> result = new java.util.ArrayList<>();
        for (String agentId : report.keys()) {
            ReportService.ClosureReport cr = report.get(agentId);
            result.add(ClosureReportDto.builder()
                    .agent(agentMapper.toDto(cr.agent))
                    .totalClosures(cr.totalClosures)
                    .totalValue(cr.totalValue)
                    .totalCommission(cr.totalCommission)
                    .build());
        }
        return ResponseEntity.ok(result);
    }
}