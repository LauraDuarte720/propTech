package co.edu.uniquindio.com.proptech.services;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.structures.arrayList.ArrayList;
import co.edu.uniquindio.com.proptech.structures.hashTable.HashTable;
import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final PropertyService propertyService;
    private final VisitService visitService;
    private final AgentService agentService;
    private final OperationService operationService;

    public ReportService(PropertyService propertyService,
                         VisitService visitService,
                         AgentService agentService,
                         OperationService operationService) {
        this.propertyService = propertyService;
        this.visitService = visitService;
        this.agentService = agentService;
        this.operationService = operationService;
    }

    // ══════════════════════════════════════════════
    // REPORTE POR ZONA
    // ══════════════════════════════════════════════

    public HashTable<String, ZoneReport> getReportByZone() {
        HashTable<String, ZoneReport> report = new HashTable<>();

        // Propiedades por ciudad
        HashTable<City, ArrayList<Property>> byCity = propertyService.getPropertiesByCity();
        for (ArrayList<Property> props : byCity.values()) {
            if (props == null || props.isEmpty()) continue;
            String city = props.get(0).getNeighborhood().getCity().toString();
            getOrCreateZoneReport(report, city).totalProperties += props.size();
        }

        // Visitas por ciudad
        LinkedList<Visit> visits = visitService.getAllVisits();
        for (Visit visit : visits) {
            if (visit.getProperty() == null || visit.getProperty().getNeighborhood() == null) continue;
            String city = visit.getProperty().getNeighborhood().getCity().toString();
            getOrCreateZoneReport(report, city).totalVisits++;
        }

        // Cierres por ciudad
        LinkedList<Operation> operations = operationService.getAllOperations();
        for (Operation op : operations) {
            if (op.getProcessStatus() != ProcessStatus.CLOSED) continue;
            if (op.getProperty() == null || op.getProperty().getNeighborhood() == null) continue;
            String city = op.getProperty().getNeighborhood().getCity().toString();
            getOrCreateZoneReport(report, city).totalClosures++;
        }

        return report;
    }

    // ══════════════════════════════════════════════
    // REPORTE POR PRECIO
    // ══════════════════════════════════════════════

    public HashTable<String, Integer> getReportByPriceRange() {
        HashTable<String, Integer> report = new HashTable<>();

        for (Property property : propertyService.getAllProperties().values()) {
            if (property.getPrice() == null) continue;
            String range = getPriceRange(property.getPrice());
            Integer current = report.get(range);
            report.put(range, current == null ? 1 : current + 1);
        }

        return report;
    }

    private String getPriceRange(Double price) {
        if (price < 100_000_000)        return "Menos de $100M";
        else if (price < 300_000_000)   return "$100M - $300M";
        else if (price < 500_000_000)   return "$300M - $500M";
        else if (price < 1_000_000_000) return "$500M - $1.000M";
        else                            return "Más de $1.000M";
    }

    // ══════════════════════════════════════════════
    // REPORTE POR VISITAS
    // ══════════════════════════════════════════════

    public ArrayList<VisitReport> getReportByVisits() {
        HashTable<String, Integer> frequency = visitService.getFrequencyByProperty();
        ArrayList<VisitReport> report = new ArrayList<>();

        for (Property property : propertyService.getAllProperties().values()) {
            Integer freq = frequency.get(property.getCode());
            report.add(new VisitReport(property, freq == null ? 0 : freq));
        }

        insertionSort(report);
        return report;
    }

    // ══════════════════════════════════════════════
    // REPORTE POR CIERRES
    // ══════════════════════════════════════════════

    public HashTable<String, ClosureReport> getReportByClosures() {
        HashTable<String, ClosureReport> report = new HashTable<>();

        LinkedList<Operation> operations = operationService.getAllOperations();
        for (Operation op : operations) {
            if (op.getProcessStatus() != ProcessStatus.CLOSED) continue;
            if (op.getAgent() == null) continue;

            String agentId = op.getAgent().getCedula();
            ClosureReport cr = report.get(agentId);
            if (cr == null) {
                cr = new ClosureReport(op.getAgent());
                report.put(agentId, cr);
            }
            cr.totalClosures++;
            cr.totalValue      += op.getValue()      == null ? 0 : op.getValue();
            cr.totalCommission += op.getCommission() == null ? 0 : op.getCommission();
        }

        return report;
    }

    // ══════════════════════════════════════════════
    // CLASES INTERNAS
    // ══════════════════════════════════════════════

    public static class ZoneReport {
        public int    totalProperties = 0;
        public int    totalVisits     = 0;
        public int    totalClosures   = 0;
    }

    public static class VisitReport {
        public Property property;
        public int      totalVisits;

        public VisitReport(Property property, int totalVisits) {
            this.property   = property;
            this.totalVisits = totalVisits;
        }
    }

    public static class ClosureReport {
        public Agent  agent;
        public int    totalClosures   = 0;
        public double totalValue      = 0;
        public double totalCommission = 0;

        public ClosureReport(Agent agent) {
            this.agent = agent;
        }
    }

    // ══════════════════════════════════════════════
    // MÉTODOS PRIVADOS DE APOYO
    // ══════════════════════════════════════════════

    private ZoneReport getOrCreateZoneReport(HashTable<String, ZoneReport> report, String key) {
        ZoneReport zr = report.get(key);
        if (zr == null) {
            zr = new ZoneReport();
            report.put(key, zr);
        }
        return zr;
    }

    private void insertionSort(ArrayList<VisitReport> list) {
        for (int i = 1; i < list.size(); i++) {
            VisitReport key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).totalVisits < key.totalVisits) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}