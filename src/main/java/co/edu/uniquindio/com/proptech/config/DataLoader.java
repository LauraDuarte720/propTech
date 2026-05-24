package co.edu.uniquindio.com.proptech.config;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.services.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Carga datos de prueba al iniciar la aplicación.
 *
 * Flujo para publicar inmuebles (workaround sin modificar el backend):
 *   1. registerProperty(agentId)  → guarda en NEW pero NO pone Agent en el objeto
 *   2. addPropertyToAgent(...)    → asigna el Agent en el objeto Property
 *   3. publishPropertyWithLog()   → verifica getAgent() != null → pasa a ACTIVE
 */
@Component
public class DataLoader {

    private final GeographicZoneService geographicZoneService;
    private final AgentService          agentService;
    private final PropertyService       propertyService;
    private final ClientService         clientService;
    private final OperationService      operationService;
    private final VisitService          visitService;
    private final NeighborhoodService   neighborhoodService;

    public DataLoader(GeographicZoneService geographicZoneService,
                      AgentService agentService,
                      PropertyService propertyService,
                      ClientService clientService,
                      OperationService operationService,
                      VisitService visitService,
                      NeighborhoodService neighborhoodService) {
        this.geographicZoneService = geographicZoneService;
        this.agentService          = agentService;
        this.propertyService       = propertyService;
        this.clientService         = clientService;
        this.operationService      = operationService;
        this.visitService          = visitService;
        this.neighborhoodService   = neighborhoodService;
    }

    @PostConstruct
    public void load() {

        // ══════════════════════════════════════════════
        // 1. ZONAS GEOGRÁFICAS
        // ══════════════════════════════════════════════

        GeographicZone zonaNorteArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder()
                        .city(City.ARMENIA).zone(Zone.NORTH).nameNeighborhood("El Bosque").build());

        GeographicZone zonaCentroArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder()
                        .city(City.ARMENIA).zone(Zone.CENTER).nameNeighborhood("La Castellana").build());

        GeographicZone zonaSurArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder()
                        .city(City.ARMENIA).zone(Zone.SOUTH).nameNeighborhood("Laureles").build());

        GeographicZone zonaOrienteMedellin = geographicZoneService.findOrCreate(
                GeographicZone.builder()
                        .city(City.MEDELLIN).zone(Zone.NORTH).nameNeighborhood("El Poblado").build());

        GeographicZone zonaOccidenteCali = geographicZoneService.findOrCreate(
                GeographicZone.builder()
                        .city(City.CALI).zone(Zone.SOUTH).nameNeighborhood("Ciudad Jardin").build());

        // ══════════════════════════════════════════════
        // 2. BARRIOS
        // ══════════════════════════════════════════════

        Neighborhood barrioElBosque = neighborhoodService.findOrCreate(
                Neighborhood.builder()
                        .city(City.ARMENIA).zone(Zone.NORTH).name("El Bosque").build());

        Neighborhood barrioLaCastellana = neighborhoodService.findOrCreate(
                Neighborhood.builder()
                        .city(City.ARMENIA).zone(Zone.CENTER).name("La Castellana").build());

        Neighborhood barrioLaureles = neighborhoodService.findOrCreate(
                Neighborhood.builder()
                        .city(City.ARMENIA).zone(Zone.SOUTH).name("Laureles").build());

        Neighborhood barrioElPoblado = neighborhoodService.findOrCreate(
                Neighborhood.builder()
                        .city(City.MEDELLIN).zone(Zone.NORTH).name("El Poblado").build());

        Neighborhood barrioCiudadJardin = neighborhoodService.findOrCreate(
                Neighborhood.builder()
                        .city(City.CALI).zone(Zone.SOUTH).name("Ciudad Jardin").build());

        // ══════════════════════════════════════════════
        // 3. ASESORES
        // ══════════════════════════════════════════════

        Agent agente1 = agentService.registerAgent(Agent.builder()
                .cedula("1094000001")
                .name("Carlos Mejia")
                .username("carlos.mejia")
                .password("Carlos123")
                .contact("+57310111001")
                .assignedZone(zonaNorteArmenia)
                .closedDeals(12)
                .build());

        Agent agente2 = agentService.registerAgent(Agent.builder()
                .cedula("1094000002")
                .name("Laura Gomez")
                .username("laura.gomez")
                .password("Laura456A")
                .contact("+57310111002")
                .assignedZone(zonaCentroArmenia)
                .closedDeals(8)
                .build());

        Agent agente3 = agentService.registerAgent(Agent.builder()
                .cedula("1094000003")
                .name("Andres Rios")
                .username("andres.rios")
                .password("Andres789")
                .contact("+57310111003")
                .assignedZone(zonaOrienteMedellin)
                .closedDeals(5)
                .build());

        Agent agente4 = agentService.registerAgent(Agent.builder()
                .cedula("1094000004")
                .name("Marcela Torres")
                .username("marcela.torres")
                .password("Marcela321")
                .contact("+57310111004")
                .assignedZone(zonaOccidenteCali)
                .closedDeals(15)
                .build());

        Agent agenteSur = agentService.registerAgent(Agent.builder()
                .cedula("1094000005")
                .name("Juliana Perez")
                .username("juliana.perez")
                    .password("Juliana123")
                .contact("+57310111005")
                .assignedZone(zonaSurArmenia)
                .closedDeals(6)
                .build());

        // ══════════════════════════════════════════════
        // 4. INMUEBLES
        // ══════════════════════════════════════════════

        Property prop1 = registrarYPublicar(
                Property.builder()
                        .address("Calle 20 # 14-35")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.SALE)
                        .price(280_000_000.0)
                        .area(75.0)
                        .numBedrooms(3)
                        .numBathrooms(2)
                        .build(),
                agente1);

        Property prop2 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 15 # 8-12")
                        .neighborhood(barrioLaCastellana)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(450_000_000.0)
                        .area(140.0)
                        .numBedrooms(4)
                        .numBathrooms(3)
                        .build(),
                agente2);

        Property prop3 = registrarYPublicar(
                Property.builder()
                        .address("Avenida Bolivar # 22-60")
                        .neighborhood(barrioLaureles)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.RENT)
                        .price(1_800_000.0)
                        .area(55.0)
                        .numBedrooms(2)
                        .numBathrooms(1)
                        .build(),
                agenteSur);

        Property prop4 = registrarYPublicar(
                Property.builder()
                        .address("Calle 10 # 43-22")
                        .neighborhood(barrioElPoblado)
                        .propertyType(PropertyType.OFFICE)
                        .purpose(Purpose.RENT)
                        .price(3_500_000.0)
                        .area(90.0)
                        .numBedrooms(0)
                        .numBathrooms(2)
                        .build(),
                agente3);

        Property prop5 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 8 # 15-40")
                        .neighborhood(barrioCiudadJardin)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(620_000_000.0)
                        .area(200.0)
                        .numBedrooms(5)
                        .numBathrooms(4)
                        .build(),
                agente4);

        Property prop6 = registrarYPublicar(
                Property.builder()
                        .address("Calle 5 # 10-80")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.LOT)
                        .purpose(Purpose.SALE)
                        .price(150_000_000.0)
                        .area(300.0)
                        .numBedrooms(0)
                        .numBathrooms(0)
                        .build(),
                agente1);

        Property prop7 = registrarYPublicar(
                Property.builder()
                        .address("Calle 33 # 25-15")
                        .neighborhood(barrioLaCastellana)
                        .propertyType(PropertyType.RETAIL_SPACE)
                        .purpose(Purpose.RENT)
                        .price(2_200_000.0)
                        .area(40.0)
                        .numBedrooms(0)
                        .numBathrooms(1)
                        .build(),
                agente2);

        // Inmueble sin publicar (INACTIVE) — confirm=true, sin agente
        propertyService.registerProperty(
                Property.builder()
                        .address("Carrera 20 # 5-30")
                        .neighborhood(barrioLaureles)
                        .propertyType(PropertyType.WAREHOUSE)
                        .purpose(Purpose.RENT)
                        .price(4_000_000.0)
                        .area(500.0)
                        .numBedrooms(0)
                        .numBathrooms(2)
                        .build(),
                null, true);

        // ══════════════════════════════════════════════
        // 5. CLIENTES
        // ══════════════════════════════════════════════

        Client cliente1 = clientService.registerClient(Client.builder()
                .cedula("1093000001")
                .name("Sofia Herrera")
                .username("sofia.herrera")
                .password("Sofia123A")
                .email("sofia@email.com")
                .phone("+57300200001")
                .budget(300_000_000.0)
                .minBedrooms(2)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.APARTMENT)
                .build());
        cliente1.addInterestZone(zonaNorteArmenia);
        cliente1.addInterestZone(zonaCentroArmenia);

        Client cliente2 = clientService.registerClient(Client.builder()
                .cedula("1093000002")
                .name("Tomas Vargas")
                .username("tomas.vargas")
                .password("Tomas456A")
                .email("tomas@email.com")
                .phone("+57300200002")
                .budget(500_000_000.0)
                .minBedrooms(3)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.HOUSE)
                .build());
        cliente2.addInterestZone(zonaCentroArmenia);

        Client cliente3 = clientService.registerClient(Client.builder()
                .cedula("1093000003")
                .name("Valentina Ospina")
                .username("valentina.ospina")
                .password("Vale789Ab")
                .email("vale@email.com")
                .phone("+57300200003")
                .budget(2_000_000.0)
                .minBedrooms(1)
                .clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.APARTMENT)
                .build());
        cliente3.addInterestZone(zonaSurArmenia);

        Client cliente4 = clientService.registerClient(Client.builder()
                .cedula("1093000004")
                .name("Felipe Castillo")
                .username("felipe.castillo")
                .password("Felipe321A")
                .email("felipe@email.com")
                .phone("+57300200004")
                .budget(700_000_000.0)
                .minBedrooms(4)
                .clientType(ClientType.INVESTOR)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.HOUSE)
                .build());
        cliente4.addInterestZone(zonaOrienteMedellin);
        cliente4.addInterestZone(zonaOccidenteCali);

        Client cliente5 = clientService.registerClient(Client.builder()
                .cedula("1093000005")
                .name("Camila Restrepo")
                .username("camila.restrepo")
                .password("Camila654A")
                .email("camila@email.com")
                .phone("+57300200005")
                .budget(400_000_000.0)
                .minBedrooms(3)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.PAUSED)
                .desiredPropertyType(PropertyType.HOUSE)
                .build());
        cliente5.addInterestZone(zonaOccidenteCali);

        // ══════════════════════════════════════════════
        // 6. INTERACCIONES
        // ══════════════════════════════════════════════

        registerInteraction(cliente1, prop1, InteractionType.VISITED,           LocalDateTime.now().minusDays(10));
        registerInteraction(cliente1, prop1, InteractionType.SAVED,             LocalDateTime.now().minusDays(8));
        registerInteraction(cliente1, prop1, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(5));
        registerInteraction(cliente1, prop3, InteractionType.VISITED,           LocalDateTime.now().minusDays(3));

        registerInteraction(cliente2, prop2, InteractionType.VISITED,           LocalDateTime.now().minusDays(12));
        registerInteraction(cliente2, prop2, InteractionType.NEGOTIATED,        LocalDateTime.now().minusDays(9));
        registerInteraction(cliente2, prop2, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(6));
        registerInteraction(cliente2, prop5, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(4));

        registerInteraction(cliente3, prop3, InteractionType.VISITED,           LocalDateTime.now().minusDays(7));
        registerInteraction(cliente3, prop3, InteractionType.RENTING_INTENTION, LocalDateTime.now().minusDays(2));

        registerInteraction(cliente4, prop5, InteractionType.VISITED,           LocalDateTime.now().minusDays(15));
        registerInteraction(cliente4, prop5, InteractionType.SAVED,             LocalDateTime.now().minusDays(13));
        registerInteraction(cliente4, prop5, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(8));
        registerInteraction(cliente4, prop4, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(5));

        registerInteraction(cliente5, prop5, InteractionType.VISITED,           LocalDateTime.now().minusDays(20));
        registerInteraction(cliente5, prop2, InteractionType.VISITED,           LocalDateTime.now().minusDays(18));

        // ══════════════════════════════════════════════
        // 7. VISITAS
        // ══════════════════════════════════════════════

        registerVisitSafe(Visit.builder()
                .client(cliente1).property(prop1).agent(agente1)
                .date(LocalDateTime.now().minusDays(9))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("El cliente mostro interes, solicito informacion sobre credito.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente2).property(prop2).agent(agente2)
                .date(LocalDateTime.now().minusDays(11))
                .visitType(VisitType.VIP)
                .postVisitNotes("Negocio precio. Muy interesado.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente3).property(prop3).agent(agenteSur)
                .date(LocalDateTime.now().minusDays(6))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("Le gusto el apartamento, quiere firmar pronto.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente4).property(prop5).agent(agente4)
                .date(LocalDateTime.now().minusDays(14))
                .visitType(VisitType.VIP)
                .postVisitNotes("Inversor analiza retorno. Hara oferta.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente5).property(prop5).agent(agente4)
                .date(LocalDateTime.now().minusDays(19))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("No tomo decision.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente1).property(prop3).agent(agenteSur)
                .date(LocalDateTime.now().plusDays(3))
                .visitType(VisitType.NORMAL)
                .build());

        // ══════════════════════════════════════════════
        // 8. OPERACIONES
        // ══════════════════════════════════════════════

        Operation op1 = operationService.registerOperation(Operation.builder()
                .property(prop1)
                .client(cliente1)
                .agent(agente1)
                .operationType(OperationType.SALE)
                .value(275_000_000.0)
                .dateInitial(LocalDate.now().minusDays(2))
                .build());

        Operation op2 = operationService.registerOperation(Operation.builder()
                .property(prop3)
                .client(cliente3)
                .agent(agenteSur)
                .operationType(OperationType.RENT)
                .value(1_800_000.0)
                .dateInitial(LocalDate.now().minusDays(3))
                .dateFinal(LocalDate.now().plusMonths(12))
                .build());

        operationService.updateOperation(Operation.builder()
                .id(op2.getId())
                .processStatus(ProcessStatus.CLOSED)
                .build());

        operationService.registerOperation(Operation.builder()
                .property(prop5)
                .client(cliente4)
                .agent(agente4)
                .operationType(OperationType.SALE)
                .value(610_000_000.0)
                .dateInitial(LocalDate.now().minusDays(1))
                .build());

        Operation op4 = operationService.registerOperation(Operation.builder()
                .property(prop2)
                .client(cliente2)
                .agent(agente2)
                .operationType(OperationType.SALE)
                .value(440_000_000.0)
                .dateInitial(LocalDate.now().minusDays(8))
                .dateFinal(LocalDate.now().minusDays(1))
                .build());

        operationService.updateOperation(Operation.builder()
                .id(op4.getId())
                .processStatus(ProcessStatus.CLOSED)
                .build());

        // ─── CONTRACT_EXPIRING ──────────────────────────────────────────────
        operationService.registerOperation(Operation.builder()
                .property(prop7)
                .client(cliente2)
                .agent(agente2)
                .operationType(OperationType.CONTRACT_RENEWAL)   // ← tipo necesario
                .value(2_200_000.0)
                .dateInitial(LocalDate.now().minusMonths(11))
                .dateFinal(LocalDate.now().plusDays(15))          // ← vence en 15 días
                .build());

// ─── RESERVE_NO_CLOSURE ─────────────────────────────────────────────
        operationService.registerOperation(Operation.builder()
                .property(prop6)
                .client(cliente4)
                .agent(agente1)
                .operationType(OperationType.SALE)
                .value(150_000_000.0)
                .dateInitial(LocalDate.now().minusDays(35))       // ← más de 30 días, sin cerrar
                .build());

// ─── INACTIVE_CLIENT ────────────────────────────────────────────────
        Client clienteInactivo = clientService.registerClient(Client.builder()
                .cedula("1093000099")
                .name("Juan Inactivo")
                .username("juan.inactivo")
                .password("Juan999Ab")
                .email("juan@email.com")
                .phone("+57300200099")
                .budget(200_000_000.0)
                .minBedrooms(2)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.PAUSED)
                .desiredPropertyType(PropertyType.APARTMENT)
                .build());
// Sin interacciones → lleva "infinito" días inactivo

        // ══════════════════════════════════════════════
// SUPPORT REQUESTS
// ══════════════════════════════════════════════

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente1)
                        .property(prop1)
                        .agent(agente1)
                        .message("Necesito informacion adicional sobre opciones de financiamiento.")
                        .date(LocalDateTime.now().minusDays(2))
                        .build()
        );

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente2)
                        .property(prop2)
                        .agent(agente2)
                        .message("Quiero agendar una nueva visita para negociar el precio.")
                        .date(LocalDateTime.now().minusDays(1))
                        .build()
        );

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente3)
                        .property(prop3)
                        .agent(agenteSur)
                        .message("Tengo dudas sobre las condiciones del contrato de arriendo.")
                        .date(LocalDateTime.now().minusHours(10))
                        .build()
        );

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente4)
                        .property(prop5)
                        .agent(agente4)
                        .message("Deseo conocer los impuestos asociados a la propiedad.")
                        .date(LocalDateTime.now().minusHours(5))
                        .build()
        );

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente5)
                        .property(prop7)
                        .agent(agente2)
                        .message("Necesito informacion sobre disponibilidad y parqueaderos.")
                        .date(LocalDateTime.now().minusMinutes(30))
                        .build()
        );

        // Atender solicitudes
        agentService.attendSupportRequest(agente2.getCedula());
        agentService.attendSupportRequest(agente4.getCedula());

// Cancelar solicitud
        SupportRequest pending = agentService.getNextSupportRequest(agente1.getCedula());

        if (pending != null) {
            agentService.cancelSupportRequest(
                    agente1.getCedula(),
                    pending.getId()
            );
        }

        System.out.println("✅ DataLoader: datos de prueba cargados correctamente.");

        // ══════════════════════════════════════════════
// 9. DATOS EXTRA PARA DISPARAR ALERTAS ANÓMALAS
// ══════════════════════════════════════════════

// HIGH_VISITS_NO_CLOSING: prop6 necesita 5+ visitas COMPLETED sin operación
// Usamos clientes existentes con agente1 (zona Norte Armenia = prop6)
        for (int i = 0; i < 6; i++) {
            Client c = (i % 2 == 0) ? cliente1 : cliente3;
            registerVisitSafe(Visit.builder()
                    .client(c).property(prop6).agent(agente1)
                    .date(LocalDateTime.now().minusDays(20 + i))
                    .visitType(VisitType.NORMAL)
                    .postVisitNotes("Visita " + (i+1) + " sin cierre")
                    .build());
        }

// CLIENT_MULTIPLE_VISITS_NO_CONTINUITY: cliente5 hace 4 visitas en 30 días sin COMPLETED
        registerVisitSafe(Visit.builder()
                .client(cliente5).property(prop6).agent(agente1)
                .date(LocalDateTime.now().minusDays(25))
                .visitType(VisitType.NORMAL).build());
        registerVisitSafe(Visit.builder()
                .client(cliente5).property(prop7).agent(agente2)
                .date(LocalDateTime.now().minusDays(15))
                .visitType(VisitType.NORMAL).build());
        registerVisitSafe(Visit.builder()
                .client(cliente5).property(prop6).agent(agente1)
                .date(LocalDateTime.now().minusDays(8))
                .visitType(VisitType.NORMAL).build());

// AGENT_EXCESSIVE_OVERLOAD: agente1 acumula 11 visitas PENDING/CONFIRMED
        for (int i = 0; i < 10; i++) {
            registerVisitSafe(Visit.builder()
                    .client(cliente1).property(prop6).agent(agente1)
                    .date(LocalDateTime.now().plusDays(5 + i))
                    .visitType(VisitType.NORMAL).build());
        }

// ZONE_INTEREST_CONCENTRATION: 6 visitas en zona NORTE en menos de 7 días
        for (int i = 0; i < 5; i++) {
            Client c = (i % 2 == 0) ? cliente2 : cliente4;
            registerVisitSafe(Visit.builder()
                    .client(c).property(prop6).agent(agente1)
                    .date(LocalDateTime.now().minusDays(i))
                    .visitType(VisitType.NORMAL).build());
        }
    }

    // ── helpers privados ──────────────────────────────────────────────────────

    /**
     * Registra un inmueble, asigna el agente en el objeto (via addPropertyToAgent)
     * y luego lo publica. Workaround para que publishProperty no falle con
     * "getAgent() == null" sin modificar el backend.
     */
    private Property registrarYPublicar(Property property, Agent agente) {
        // 1. Registrar con agentId → queda en NEW
        Property saved = propertyService.registerProperty(property, agente.getCedula(), false);
        // 2. Asignar agente en el objeto Property via el servicio de agentes
        agentService.addPropertyToAgent(saved.getCode(), agente.getCedula());
        // 3. Publicar — ahora getAgent() != null
        return propertyService.publishPropertyWithLog(saved.getCode());
    }

    private void registerInteraction(Client client, Property property,
                                     InteractionType type, LocalDateTime timestamp) {
        UserInteraction interaction = UserInteraction.builder()
                .client(client)
                .property(property)
                .interactionType(type)
                .timestamp(timestamp)
                .build();
        clientService.registerUserInteraction(interaction);
    }

    private void registerVisitSafe(Visit visit) {
        try {
            visitService.registerVisit(visit);
        } catch (RuntimeException e) {
            System.out.println("⚠️  Visita omitida por conflicto de agenda: " + e.getMessage());
        }
    }


}