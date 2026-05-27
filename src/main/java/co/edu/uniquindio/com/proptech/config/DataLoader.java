package co.edu.uniquindio.com.proptech.config;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.services.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DataLoader — datos de prueba coherentes con la lógica de servicios.
 *
 * ─── Reglas de estado de visitas ────────────────────────────────────────────
 *  COMPLETED  : solo mediante agentService.attendVisit() (dequeue). Requiere que
 *               la visita esté CONFIRMED primero. Se usa para visitas del pasado
 *               que ya ocurrieron y de las que queremos registrar el resultado.
 *  CONFIRMED  : visitService.confirmVisit(visit). Visitas del pasado reciente
 *               o del futuro que fueron confirmadas pero no procesadas aún.
 *  PENDING    : estado inicial tras registerVisit(). Visitas futuras sin confirmar.
 *  EXPIRED    : lo pone automáticamente updateExpiredVisits() si fecha+1d < now
 *               y la visita no está COMPLETED/CANCELED.
 *
 * ─── Alertas básicas disparadas ─────────────────────────────────────────────
 *  CONTRACT_EXPIRING          → op de tipo CONTRACT_RENEWAL con dateFinal ≤ 30 días
 *  PROPERTY_NO_VISITS         → inmueble sin visita en los últimos 60 días
 *  HIGH_DEMAND                → propiedad con > 5 visitas en los últimos 30 días
 *  PENDING_VISIT_CONFIRMATION → visita PENDING con createdAt > 24 h
 *  RESERVE_NO_CLOSURE         → operación CREATED hace > 30 días sin cerrar
 *  INACTIVE_CLIENT            → cliente sin interacción en los últimos 30 días
 *
 * ─── Alertas anómalas disparadas ────────────────────────────────────────────
 *  HIGH_VISITS_NO_CLOSING           → ≥ 5 visitas COMPLETED en propiedad sin op SALE/RENT
 *  CLIENT_MULTIPLE_VISITS_NO_CONTINUITY → ≥ 3 visitas en 30 días sin ninguna COMPLETED
 *  AGENT_EXCESSIVE_OVERLOAD         → ≥ 10 visitas PENDING/CONFIRMED para un agente
 *  PROPERTY_PRICE_CHANGE            → ≥ 3 cambios de precio en 30 días
 *  ZONE_INTEREST_CONCENTRATION      → ≥ 5 visitas en 7 días en la misma zona
 *
 * ─── ORDEN CRÍTICO ──────────────────────────────────────────────────────────
 *  Las visitas históricas (sección 6) deben registrarse ANTES de cerrar las
 *  operaciones (sección 7), porque cerrar una operación SALE/RENT cambia el
 *  estado de la propiedad a SOLD/RENTED, impidiendo nuevas visitas.
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

        // ══════════════════════════════════════════════════════════════════════
        // 1. ZONAS GEOGRÁFICAS
        // ══════════════════════════════════════════════════════════════════════

        GeographicZone zonaNorteArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder().city(City.ARMENIA).zone(Zone.NORTH).nameNeighborhood("El Bosque").build());

        GeographicZone zonaCentroArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder().city(City.ARMENIA).zone(Zone.CENTER).nameNeighborhood("La Castellana").build());

        GeographicZone zonaSurArmenia = geographicZoneService.findOrCreate(
                GeographicZone.builder().city(City.ARMENIA).zone(Zone.SOUTH).nameNeighborhood("Laureles").build());

        GeographicZone zonaNorteMedellin = geographicZoneService.findOrCreate(
                GeographicZone.builder().city(City.MEDELLIN).zone(Zone.NORTH).nameNeighborhood("El Poblado").build());

        GeographicZone zonaSurCali = geographicZoneService.findOrCreate(
                GeographicZone.builder().city(City.CALI).zone(Zone.SOUTH).nameNeighborhood("Ciudad Jardin").build());

        // ══════════════════════════════════════════════════════════════════════
        // 2. BARRIOS
        // ══════════════════════════════════════════════════════════════════════

        Neighborhood barrioElBosque = neighborhoodService.findOrCreate(
                Neighborhood.builder().city(City.ARMENIA).zone(Zone.NORTH).name("El Bosque").build());

        Neighborhood barrioLaCastellana = neighborhoodService.findOrCreate(
                Neighborhood.builder().city(City.ARMENIA).zone(Zone.CENTER).name("La Castellana").build());

        Neighborhood barrioLaureles = neighborhoodService.findOrCreate(
                Neighborhood.builder().city(City.ARMENIA).zone(Zone.SOUTH).name("Laureles").build());

        Neighborhood barrioElPoblado = neighborhoodService.findOrCreate(
                Neighborhood.builder().city(City.MEDELLIN).zone(Zone.NORTH).name("El Poblado").build());

        Neighborhood barrioCiudadJardin = neighborhoodService.findOrCreate(
                Neighborhood.builder().city(City.CALI).zone(Zone.SOUTH).name("Ciudad Jardin").build());

        // ══════════════════════════════════════════════════════════════════════
        // 3. ASESORES
        // ══════════════════════════════════════════════════════════════════════

        Agent agente1 = agentService.registerAgent(Agent.builder()
                .cedula("1094000001").name("Carlos Mejia").username("carlos.mejia")
                .password("Carlos123").contact("+57310111001")
                .assignedZone(zonaNorteArmenia).closedDeals(12).build());

        Agent agente2 = agentService.registerAgent(Agent.builder()
                .cedula("1094000002").name("Laura Gomez").username("laura")
                .password("Laura123").contact("+57310111002")
                .assignedZone(zonaCentroArmenia).closedDeals(8).build());

        Agent agente3 = agentService.registerAgent(Agent.builder()
                .cedula("1094000003").name("Andres Rios").username("Joab")
                .password("Joab123").contact("+57310111003")
                .assignedZone(zonaNorteMedellin).closedDeals(5).build());

        Agent agente4 = agentService.registerAgent(Agent.builder()
                .cedula("1094000004").name("Marcela Torres").username("marcela.torres")
                .password("Marcela321").contact("+57310111004")
                .assignedZone(zonaSurCali).closedDeals(15).build());

        Agent agente5 = agentService.registerAgent(Agent.builder()
                .cedula("1094000005").name("Juliana Perez").username("juliana.perez")
                .password("Juliana123").contact("+57310111005")
                .assignedZone(zonaSurArmenia).closedDeals(6).build());

        // ══════════════════════════════════════════════════════════════════════
        // 4. INMUEBLES (25 activos + 1 sin publicar)
        // ══════════════════════════════════════════════════════════════════════

        // ── Norte Armenia (agente1) ──
        Property propA1 = publicar(Property.builder()
                .address("Calle 20 # 14-35").neighborhood(barrioElBosque)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.SALE)
                .price(280_000_000.0).area(75.0).numBedrooms(3).numBathrooms(2).build(), agente1);

        Property propA2 = publicar(Property.builder()
                .address("Calle 18 # 12-50").neighborhood(barrioElBosque)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.SALE)
                .price(290_000_000.0).area(80.0).numBedrooms(3).numBathrooms(2).build(), agente1);

        Property propA3 = publicar(Property.builder()
                .address("Carrera 14 # 20-30").neighborhood(barrioElBosque)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.SALE)
                .price(255_000_000.0).area(68.0).numBedrooms(2).numBathrooms(2).build(), agente1);

        Property propA4 = publicar(Property.builder()
                .address("Calle 22 # 11-15").neighborhood(barrioElBosque)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(310_000_000.0).area(95.0).numBedrooms(3).numBathrooms(2).build(), agente1);

        Property propA5 = publicar(Property.builder()
                .address("Calle 5 # 10-80").neighborhood(barrioElBosque)
                .propertyType(PropertyType.LOT).purpose(Purpose.SALE)
                .price(150_000_000.0).area(300.0).numBedrooms(0).numBathrooms(0).build(), agente1);

        Property propA6 = publicar(Property.builder()
                .address("Avenida El Dorado # 45-12").neighborhood(barrioElBosque)
                .propertyType(PropertyType.OFFICE).purpose(Purpose.RENT)
                .price(4_000_000.0).area(150.0).numBedrooms(0).numBathrooms(3).build(), agente1);

        // ── Centro Armenia (agente2) ──
        Property propC1 = publicar(Property.builder()
                .address("Carrera 15 # 8-12").neighborhood(barrioLaCastellana)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(450_000_000.0).area(140.0).numBedrooms(4).numBathrooms(3).build(), agente2);

        Property propC2 = publicar(Property.builder()
                .address("Carrera 17 # 9-40").neighborhood(barrioLaCastellana)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.SALE)
                .price(275_000_000.0).area(72.0).numBedrooms(3).numBathrooms(2).build(), agente2);

        Property propC3 = publicar(Property.builder()
                .address("Calle 12 # 16-22").neighborhood(barrioLaCastellana)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.RENT)
                .price(1_500_000.0).area(60.0).numBedrooms(2).numBathrooms(1).build(), agente2);

        Property propC4 = publicar(Property.builder()
                .address("Calle 33 # 25-15").neighborhood(barrioLaCastellana)
                .propertyType(PropertyType.RETAIL_SPACE).purpose(Purpose.RENT)
                .price(2_200_000.0).area(40.0).numBedrooms(0).numBathrooms(1).build(), agente2);

        Property propC5 = publicar(Property.builder()
                .address("Carrera 7 # 12-34").neighborhood(barrioLaCastellana)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.SALE)
                .price(350_000_000.0).area(90.0).numBedrooms(3).numBathrooms(2).build(), agente2);

        // ── Sur Armenia (agente5) ──
        Property propS1 = publicar(Property.builder()
                .address("Avenida Bolivar # 22-60").neighborhood(barrioLaureles)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.RENT)
                .price(1_800_000.0).area(55.0).numBedrooms(2).numBathrooms(1).build(), agente5);

        Property propS2 = publicar(Property.builder()
                .address("Avenida Centenario # 5-18").neighborhood(barrioLaureles)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.RENT)
                .price(1_700_000.0).area(58.0).numBedrooms(2).numBathrooms(1).build(), agente5);

        Property propS3 = publicar(Property.builder()
                .address("Calle 3 # 8-55").neighborhood(barrioLaureles)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.RENT)
                .price(1_950_000.0).area(65.0).numBedrooms(2).numBathrooms(2).build(), agente5);

        Property propS4 = publicar(Property.builder()
                .address("Carrera 5 # 3-10").neighborhood(barrioLaureles)
                .propertyType(PropertyType.LOT).purpose(Purpose.SALE)
                .price(200_000_000.0).area(500.0).numBedrooms(0).numBathrooms(0).build(), agente5);

        // ── Medellín El Poblado (agente3) ──
        Property propM1 = publicar(Property.builder()
                .address("Calle 10 # 43-22").neighborhood(barrioElPoblado)
                .propertyType(PropertyType.OFFICE).purpose(Purpose.RENT)
                .price(3_500_000.0).area(90.0).numBedrooms(0).numBathrooms(2).build(), agente3);

        Property propM2 = publicar(Property.builder()
                .address("Carrera 43A # 10-30").neighborhood(barrioElPoblado)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(650_000_000.0).area(210.0).numBedrooms(5).numBathrooms(4).build(), agente3);

        Property propM3 = publicar(Property.builder()
                .address("Calle 8 Sur # 42-80").neighborhood(barrioElPoblado)
                .propertyType(PropertyType.APARTMENT).purpose(Purpose.RENT)
                .price(4_200_000.0).area(110.0).numBedrooms(3).numBathrooms(3).build(), agente3);

        Property propM4 = publicar(Property.builder()
                .address("Calle 100 # 15-20").neighborhood(barrioElPoblado)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.RENT)
                .price(6_000_000.0).area(250.0).numBedrooms(4).numBathrooms(4).build(), agente3);

        // ── Cali Ciudad Jardín (agente4) ──
        Property propK1 = publicar(Property.builder()
                .address("Carrera 8 # 15-40").neighborhood(barrioCiudadJardin)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(620_000_000.0).area(200.0).numBedrooms(5).numBathrooms(4).build(), agente4);

        Property propK2 = publicar(Property.builder()
                .address("Carrera 100 # 11-45").neighborhood(barrioCiudadJardin)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(580_000_000.0).area(185.0).numBedrooms(4).numBathrooms(3).build(), agente4);

        Property propK3 = publicar(Property.builder()
                .address("Calle 13 Oeste # 99-20").neighborhood(barrioCiudadJardin)
                .propertyType(PropertyType.HOUSE).purpose(Purpose.SALE)
                .price(690_000_000.0).area(230.0).numBedrooms(5).numBathrooms(4).build(), agente4);

        Property propK4 = publicar(Property.builder()
                .address("Carrera 98 # 14-60").neighborhood(barrioCiudadJardin)
                .propertyType(PropertyType.OFFICE).purpose(Purpose.RENT)
                .price(5_500_000.0).area(120.0).numBedrooms(0).numBathrooms(2).build(), agente4);

        Property propK5 = publicar(Property.builder()
                .address("Calle 50 # 10-20").neighborhood(barrioCiudadJardin)
                .propertyType(PropertyType.RETAIL_SPACE).purpose(Purpose.SALE)
                .price(800_000_000.0).area(120.0).numBedrooms(0).numBathrooms(2).build(), agente4);

        // Inmueble sin publicar (INACTIVE) — para que exista estado NEW
        propertyService.registerProperty(
                Property.builder()
                        .address("Carrera 20 # 5-30").neighborhood(barrioLaureles)
                        .propertyType(PropertyType.WAREHOUSE).purpose(Purpose.RENT)
                        .price(4_000_000.0).area(500.0).numBedrooms(0).numBathrooms(2).build(),
                null, true);

        // ══════════════════════════════════════════════════════════════════════
        // 5. CLIENTES (11 regulares + 1 inactivo)
        // ══════════════════════════════════════════════════════════════════════

        Client sofia = clientService.registerClient(Client.builder()
                .cedula("1093000001").name("Sofia Herrera").username("sofia.herrera")
                .password("Sofia123A").email("sofia@email.com").phone("+57300200001")
                .budget(300_000_000.0).minBedrooms(2).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.APARTMENT).build());
        sofia.addInterestZone(zonaNorteArmenia);
        sofia.addInterestZone(zonaCentroArmenia);

        Client tomas = clientService.registerClient(Client.builder()
                .cedula("1093000002").name("Tomas Vargas").username("tomas.vargas")
                .password("Tomas456A").email("tomas@email.com").phone("+57300200002")
                .budget(500_000_000.0).minBedrooms(3).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.HOUSE).build());
        tomas.addInterestZone(zonaCentroArmenia);

        Client valentina = clientService.registerClient(Client.builder()
                .cedula("1093000003").name("Valentina Ospina").username("valentina.ospina")
                .password("Vale789Ab").email("vale@email.com").phone("+57300200003")
                .budget(2_000_000.0).minBedrooms(1).clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.APARTMENT).build());
        valentina.addInterestZone(zonaSurArmenia);

        Client felipe = clientService.registerClient(Client.builder()
                .cedula("1093000004").name("Felipe Castillo").username("felipe.castillo")
                .password("Felipe321A").email("felipe@email.com").phone("+57300200004")
                .budget(700_000_000.0).minBedrooms(4).clientType(ClientType.INVESTOR)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.HOUSE).build());
        felipe.addInterestZone(zonaNorteMedellin);
        felipe.addInterestZone(zonaSurCali);

        Client camila = clientService.registerClient(Client.builder()
                .cedula("1093000005").name("Camila Restrepo").username("camila.restrepo")
                .password("Camila654A").email("camila@email.com").phone("+57300200005")
                .budget(400_000_000.0).minBedrooms(3).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.PAUSED).desiredPropertyType(PropertyType.HOUSE).build());
        camila.addInterestZone(zonaSurCali);

        Client diego = clientService.registerClient(Client.builder()
                .cedula("1093000006").name("Diego Lopez").username("diego.lopez")
                .password("Diego123A").email("diego@email.com").phone("+57300200006")
                .budget(350_000_000.0).minBedrooms(2).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.APARTMENT).build());
        diego.addInterestZone(zonaNorteArmenia);

        Client andrea = clientService.registerClient(Client.builder()
                .cedula("1093000007").name("Andrea Martinez").username("andrea.martinez")
                .password("Andrea456A").email("andrea@email.com").phone("+57300200007")
                .budget(5_000_000.0).minBedrooms(3).clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.HOUSE).build());
        andrea.addInterestZone(zonaNorteMedellin);

        Client jorge = clientService.registerClient(Client.builder()
                .cedula("1093000008").name("Jorge Ruiz").username("jorge.ruiz")
                .password("Jorge789A").email("jorge@email.com").phone("+57300200008")
                .budget(900_000_000.0).minBedrooms(4).clientType(ClientType.INVESTOR)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.RETAIL_SPACE).build());
        jorge.addInterestZone(zonaSurCali);

        Client paula = clientService.registerClient(Client.builder()
                .cedula("1093000009").name("Paula Silva").username("paula.silva")
                .password("Paula321A").email("paula@email.com").phone("+57300200009")
                .budget(250_000_000.0).minBedrooms(0).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.LOT).build());
        paula.addInterestZone(zonaSurArmenia);

        Client luis = clientService.registerClient(Client.builder()
                .cedula("1093000010").name("Luis Diaz").username("luis.diaz")
                .password("Luis654A").email("luis@email.com").phone("+57300200010")
                .budget(4_500_000.0).minBedrooms(2).clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.OFFICE).build());
        luis.addInterestZone(zonaNorteArmenia);

        Client mariela = clientService.registerClient(Client.builder()
                .cedula("1093000011").name("Mariela Cano").username("mariela.cano")
                .password("Mariela11A").email("mariela@email.com").phone("+57300200011")
                .budget(600_000_000.0).minBedrooms(4).clientType(ClientType.INVESTOR)
                .searchStatus(SearchStatus.ACTIVE).desiredPropertyType(PropertyType.HOUSE).build());
        mariela.addInterestZone(zonaSurCali);
        mariela.addInterestZone(zonaNorteMedellin);

        // Cliente sin interaccion alguna — dispara INACTIVE_CLIENT
        Client clienteInactivo = clientService.registerClient(Client.builder()
                .cedula("1093000099").name("Juan Inactivo").username("juan.inactivo")
                .password("Juan999Ab").email("juan@email.com").phone("+57300200099")
                .budget(200_000_000.0).minBedrooms(2).clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.PAUSED).desiredPropertyType(PropertyType.APARTMENT).build());
        // Sin zonas ni interacciones — lleva "infinito" dias sin actividad

        // ══════════════════════════════════════════════════════════════════════
        // 6. INTERACCIONES (multiples tipos por cliente, coherentes con las visitas)
        // ══════════════════════════════════════════════════════════════════════

        // sofia — compro propA1 (ya operacion cerrada). Ahora busca propA2
        interaccion(sofia, propA1, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(55));
        interaccion(sofia, propA1, InteractionType.SAVED,              LocalDateTime.now().minusDays(52));
        interaccion(sofia, propA1, InteractionType.VISITED,            LocalDateTime.now().minusDays(49));
        interaccion(sofia, propA1, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(47));
        interaccion(sofia, propA2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(20));
        interaccion(sofia, propA2, InteractionType.SAVED,              LocalDateTime.now().minusDays(18));
        interaccion(sofia, propA3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(10));
        interaccion(sofia, propC2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(5));

        // tomas — compro propC1. Ahora mira propC5
        interaccion(tomas, propC1, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(40));
        interaccion(tomas, propC1, InteractionType.VISITED,            LocalDateTime.now().minusDays(38));
        interaccion(tomas, propC1, InteractionType.NEGOTIATED,         LocalDateTime.now().minusDays(18));
        interaccion(tomas, propC1, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(14));
        interaccion(tomas, propC5, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(6));
        interaccion(tomas, propC5, InteractionType.SAVED,              LocalDateTime.now().minusDays(4));
        interaccion(tomas, propA4, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(2));

        // valentina — arrendo propS1. Ahora considera propS3
        interaccion(valentina, propS1, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(30));
        interaccion(valentina, propS1, InteractionType.VISITED,           LocalDateTime.now().minusDays(28));
        interaccion(valentina, propS1, InteractionType.RENTING_INTENTION, LocalDateTime.now().minusDays(24));
        interaccion(valentina, propS3, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(7));
        interaccion(valentina, propS3, InteractionType.SAVED,             LocalDateTime.now().minusDays(5));
        interaccion(valentina, propS2, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(3));

        // felipe — compro propK1. Analiza propM2 y propK3 como inversiones
        interaccion(felipe, propK1, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(45));
        interaccion(felipe, propK1, InteractionType.VISITED,            LocalDateTime.now().minusDays(43));
        interaccion(felipe, propK1, InteractionType.NEGOTIATED,         LocalDateTime.now().minusDays(22));
        interaccion(felipe, propK1, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(20));
        interaccion(felipe, propM2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(14));
        interaccion(felipe, propM2, InteractionType.SAVED,              LocalDateTime.now().minusDays(12));
        interaccion(felipe, propK3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(8));
        interaccion(felipe, propK3, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(4));

        // camila — pausada, consulto propK2 y propK3 hace tiempo
        interaccion(camila, propK2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(60));
        interaccion(camila, propK3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(55));
        interaccion(camila, propK2, InteractionType.SAVED,              LocalDateTime.now().minusDays(50));

        // diego — activo, mira apartamentos en norte Armenia
        interaccion(diego, propA2, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(22));
        interaccion(diego, propA3, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(20));
        interaccion(diego, propA3, InteractionType.SAVED,               LocalDateTime.now().minusDays(18));
        interaccion(diego, propA2, InteractionType.SAVED,               LocalDateTime.now().minusDays(15));
        interaccion(diego, propA2, InteractionType.BUYING_INTENTION,    LocalDateTime.now().minusDays(8));
        interaccion(diego, propC2, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(3));

        // andrea — arrendo propM4. Ahora considera propM3
        interaccion(andrea, propM4, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(45));
        interaccion(andrea, propM4, InteractionType.VISITED,            LocalDateTime.now().minusDays(43));
        interaccion(andrea, propM4, InteractionType.RENTING_INTENTION,  LocalDateTime.now().minusDays(38));
        interaccion(andrea, propM3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(10));
        interaccion(andrea, propM3, InteractionType.SAVED,              LocalDateTime.now().minusDays(8));
        interaccion(andrea, propM1, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(4));

        // jorge — compro propK5. Evalua propK4 para negocio
        interaccion(jorge, propK5, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(50));
        interaccion(jorge, propK5, InteractionType.VISITED,             LocalDateTime.now().minusDays(48));
        interaccion(jorge, propK5, InteractionType.NEGOTIATED,          LocalDateTime.now().minusDays(32));
        interaccion(jorge, propK5, InteractionType.BUYING_INTENTION,    LocalDateTime.now().minusDays(30));
        interaccion(jorge, propK4, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(9));
        interaccion(jorge, propK4, InteractionType.SAVED,               LocalDateTime.now().minusDays(7));
        interaccion(jorge, propK4, InteractionType.RENTING_INTENTION,   LocalDateTime.now().minusDays(2));

        // paula — compro propS4. Ve propS2 y propA5
        interaccion(paula, propS4, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(35));
        interaccion(paula, propS4, InteractionType.VISITED,             LocalDateTime.now().minusDays(33));
        interaccion(paula, propS4, InteractionType.BUYING_INTENTION,    LocalDateTime.now().minusDays(12));
        interaccion(paula, propS2, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(6));
        interaccion(paula, propA5, InteractionType.CONSULTED,           LocalDateTime.now().minusDays(2));

        // luis — arrendo propA6. Ahora busca otra oficina
        interaccion(luis, propA6, InteractionType.CONSULTED,            LocalDateTime.now().minusDays(25));
        interaccion(luis, propA6, InteractionType.VISITED,              LocalDateTime.now().minusDays(23));
        interaccion(luis, propA6, InteractionType.RENTING_INTENTION,    LocalDateTime.now().minusDays(10));
        interaccion(luis, propM1, InteractionType.CONSULTED,            LocalDateTime.now().minusDays(5));
        interaccion(luis, propK4, InteractionType.CONSULTED,            LocalDateTime.now().minusDays(3));
        interaccion(luis, propK4, InteractionType.SAVED,                LocalDateTime.now().minusDays(1));

        // mariela — inversora, interesa propM2, propK2, propK3
        interaccion(mariela, propM2, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(18));
        interaccion(mariela, propM2, InteractionType.SAVED,             LocalDateTime.now().minusDays(16));
        interaccion(mariela, propM2, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(10));
        interaccion(mariela, propK2, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(7));
        interaccion(mariela, propK3, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(5));
        interaccion(mariela, propK3, InteractionType.BUYING_INTENTION,  LocalDateTime.now().minusDays(2));

        // ══════════════════════════════════════════════════════════════════════
        // 7. VISITAS CON HISTORIA — se registran ANTES de cerrar operaciones
        //    para que las propiedades aun estén ACTIVE cuando se registran.
        //
        //    La transicion a COMPLETED es: PENDING -> CONFIRMED -> attendVisit()
        //    attendVisit() consume la cabeza de la cola del agente, así que
        //    registramos cada visita y luego llamamos attendVisit() una sola vez.
        // ══════════════════════════════════════════════════════════════════════

        // ── sofia visito propA1 (luego comprada) ── COMPLETED
        Visit vSofiaA1 = visitaSafe(sofia, propA1, agente1,
                LocalDateTime.now().minusDays(49), VisitType.NORMAL,
                "Sofia muy interesada. Solicito informacion de credito.");
        if (vSofiaA1 != null) {
            visitService.confirmVisit(vSofiaA1);
            agentService.attendVisit(agente1.getCedula());
        }

        // ── tomas visito propC1 (luego comprada) ── COMPLETED
        Visit vTomasC1 = visitaSafe(tomas, propC1, agente2,
                LocalDateTime.now().minusDays(38), VisitType.VIP,
                "Negocio precio. Muy interesado en cerrar.");
        if (vTomasC1 != null) {
            visitService.confirmVisit(vTomasC1);
            agentService.attendVisit(agente2.getCedula());
        }

        // ── valentina visito propS1 (luego arrendada) ── COMPLETED
        Visit vValeS1 = visitaSafe(valentina, propS1, agente5,
                LocalDateTime.now().minusDays(28), VisitType.NORMAL,
                "Le gusto el apartamento. Lista para firmar contrato.");
        if (vValeS1 != null) {
            visitService.confirmVisit(vValeS1);
            agentService.attendVisit(agente5.getCedula());
        }

        // ── felipe visito propK1 (luego comprada) ── COMPLETED
        Visit vFelipeK1 = visitaSafe(felipe, propK1, agente4,
                LocalDateTime.now().minusDays(43), VisitType.VIP,
                "Inversor analiza retorno esperado. Hara oferta formal.");
        if (vFelipeK1 != null) {
            visitService.confirmVisit(vFelipeK1);
            agentService.attendVisit(agente4.getCedula());
        }

        // ── andrea visito propM4 (luego arrendada) ── COMPLETED
        Visit vAndreaM4 = visitaSafe(andrea, propM4, agente3,
                LocalDateTime.now().minusDays(43), VisitType.NORMAL,
                "Le encanto la casa. Lista para rentar de inmediato.");
        if (vAndreaM4 != null) {
            visitService.confirmVisit(vAndreaM4);
            agentService.attendVisit(agente3.getCedula());
        }

        // ── jorge visito propK5 (luego comprada) ── COMPLETED (2 visitas)
        Visit vJorgeK5 = visitaSafe(jorge, propK5, agente4,
                LocalDateTime.now().minusDays(48), VisitType.VIP,
                "Evalua el local para franquicia. Va a negociar.");
        if (vJorgeK5 != null) {
            visitService.confirmVisit(vJorgeK5);
            agentService.attendVisit(agente4.getCedula());
        }
        Visit vJorgeK5b = visitaSafe(jorge, propK5, agente4,
                LocalDateTime.now().minusDays(38), VisitType.VIP,
                "Segunda visita con arquitecto. Negociacion avanzada.");
        if (vJorgeK5b != null) {
            visitService.confirmVisit(vJorgeK5b);
            agentService.attendVisit(agente4.getCedula());
        }

        // ── paula visito propS4 (luego comprada) ── COMPLETED
        Visit vPaulaS4 = visitaSafe(paula, propS4, agente5,
                LocalDateTime.now().minusDays(33), VisitType.NORMAL,
                "El lote es ideal para su proyecto de construccion.");
        if (vPaulaS4 != null) {
            visitService.confirmVisit(vPaulaS4);
            agentService.attendVisit(agente5.getCedula());
        }

        // ── luis visito propA6 (luego arrendada) ── COMPLETED
        Visit vLuisA6 = visitaSafe(luis, propA6, agente1,
                LocalDateTime.now().minusDays(23), VisitType.NORMAL,
                "Le gusto la oficina. Preguntara por adecuaciones.");
        if (vLuisA6 != null) {
            visitService.confirmVisit(vLuisA6);
            agentService.attendVisit(agente1.getCedula());
        }

        // ── camila visito propK2 (exploracion, sin cierre) ── COMPLETED
        Visit vCamilaK2 = visitaSafe(camila, propK2, agente4,
                LocalDateTime.now().minusDays(55), VisitType.NORMAL,
                "No tomo decision, esta en pausa por temas personales.");
        if (vCamilaK2 != null) {
            visitService.confirmVisit(vCamilaK2);
            agentService.attendVisit(agente4.getCedula());
        }

        // ══════════════════════════════════════════════════════════════════════
        // 8. OPERACIONES CERRADAS (base historica)
        //    Se registran DESPUES de las visitas para no bloquear el acceso
        //    a las propiedades durante el registro de visitas historicas.
        // ══════════════════════════════════════════════════════════════════════

        // propA1 → vendida a sofia (operacion cerrada, 45 dias atras)
        Operation opSofiaA1 = operationService.registerOperation(Operation.builder()
                .property(propA1).client(sofia).agent(agente1)
                .operationType(OperationType.SALE).value(275_000_000.0)
                .dateInitial(LocalDate.now().minusDays(50))
                .dateFinal(LocalDate.now().minusDays(45)).build());
        operationService.updateOperation(Operation.builder()
                .id(opSofiaA1.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propS1 → arrendada a valentina (cerrada, 20 dias atras)
        Operation opValeS1 = operationService.registerOperation(Operation.builder()
                .property(propS1).client(valentina).agent(agente5)
                .operationType(OperationType.RENT).value(1_800_000.0)
                .dateInitial(LocalDate.now().minusDays(25))
                .dateFinal(LocalDate.now().plusMonths(12)).build());
        operationService.updateOperation(Operation.builder()
                .id(opValeS1.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propC1 → vendida a tomas (cerrada, 10 dias atras)
        Operation opTomasC1 = operationService.registerOperation(Operation.builder()
                .property(propC1).client(tomas).agent(agente2)
                .operationType(OperationType.SALE).value(440_000_000.0)
                .dateInitial(LocalDate.now().minusDays(15))
                .dateFinal(LocalDate.now().minusDays(10)).build());
        operationService.updateOperation(Operation.builder()
                .id(opTomasC1.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propK1 → vendida a felipe (cerrada, 12 dias atras)
        Operation opFelipeK1 = operationService.registerOperation(Operation.builder()
                .property(propK1).client(felipe).agent(agente4)
                .operationType(OperationType.SALE).value(610_000_000.0)
                .dateInitial(LocalDate.now().minusDays(18))
                .dateFinal(LocalDate.now().minusDays(12)).build());
        operationService.updateOperation(Operation.builder()
                .id(opFelipeK1.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propM4 → arrendada a andrea (cerrada, 30 dias atras)
        Operation opAndreaM4 = operationService.registerOperation(Operation.builder()
                .property(propM4).client(andrea).agent(agente3)
                .operationType(OperationType.RENT).value(6_000_000.0)
                .dateInitial(LocalDate.now().minusDays(40))
                .dateFinal(LocalDate.now().plusMonths(12)).build());
        operationService.updateOperation(Operation.builder()
                .id(opAndreaM4.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propS4 → vendida a paula (cerrada, 5 dias atras)
        Operation opPaulaS4 = operationService.registerOperation(Operation.builder()
                .property(propS4).client(paula).agent(agente5)
                .operationType(OperationType.SALE).value(195_000_000.0)
                .dateInitial(LocalDate.now().minusDays(10))
                .dateFinal(LocalDate.now().minusDays(5)).build());
        operationService.updateOperation(Operation.builder()
                .id(opPaulaS4.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propK5 → vendida a jorge (cerrada, 3 dias atras)
        Operation opJorgeK5 = operationService.registerOperation(Operation.builder()
                .property(propK5).client(jorge).agent(agente4)
                .operationType(OperationType.SALE).value(780_000_000.0)
                .dateInitial(LocalDate.now().minusDays(28))
                .dateFinal(LocalDate.now().minusDays(3)).build());
        operationService.updateOperation(Operation.builder()
                .id(opJorgeK5.getId()).processStatus(ProcessStatus.CLOSED).build());

        // propA6 → arrendada a luis (cerrada, 8 dias atras)
        Operation opLuisA6 = operationService.registerOperation(Operation.builder()
                .property(propA6).client(luis).agent(agente1)
                .operationType(OperationType.RENT).value(3_900_000.0)
                .dateInitial(LocalDate.now().minusDays(8)).build());
        operationService.updateOperation(Operation.builder()
                .id(opLuisA6.getId()).processStatus(ProcessStatus.CLOSED).build());

        // ══════════════════════════════════════════════════════════════════════
        // 9. VISITAS FUTURAS CONFIRMADAS (proximas agendadas)
        //    Propiedades aun ACTIVE despues de las operaciones cerradas.
        // ══════════════════════════════════════════════════════════════════════

        Visit vSofiaA2 = visitaSafe(sofia, propA2, agente1,
                LocalDateTime.now().plusDays(2), VisitType.NORMAL, null);
        if (vSofiaA2 != null) visitService.confirmVisit(vSofiaA2);

        Visit vDiegoA2 = visitaSafe(diego, propA2, agente1,
                LocalDateTime.now().plusDays(3), VisitType.NORMAL, null);
        if (vDiegoA2 != null) visitService.confirmVisit(vDiegoA2);

        Visit vTomasC5 = visitaSafe(tomas, propC5, agente2,
                LocalDateTime.now().plusDays(4), VisitType.NORMAL, null);
        if (vTomasC5 != null) visitService.confirmVisit(vTomasC5);

        Visit vMarielaM2 = visitaSafe(mariela, propM2, agente3,
                LocalDateTime.now().plusDays(5), VisitType.VIP, null);
        if (vMarielaM2 != null) visitService.confirmVisit(vMarielaM2);

        Visit vFelipeK3 = visitaSafe(felipe, propK3, agente4,
                LocalDateTime.now().plusDays(6), VisitType.VIP, null);
        if (vFelipeK3 != null) visitService.confirmVisit(vFelipeK3);

        Visit vLuisM1 = visitaSafe(luis, propM1, agente3,
                LocalDateTime.now().plusDays(8), VisitType.NORMAL, null);
        if (vLuisM1 != null) visitService.confirmVisit(vLuisM1);

        Visit vValeS3 = visitaSafe(valentina, propS3, agente5,
                LocalDateTime.now().plusDays(3), VisitType.NORMAL, null);
        if (vValeS3 != null) visitService.confirmVisit(vValeS3);

        Visit vAndreaM3 = visitaSafe(andrea, propM3, agente3,
                LocalDateTime.now().plusDays(7), VisitType.NORMAL, null);
        if (vAndreaM3 != null) visitService.confirmVisit(vAndreaM3);

        // ══════════════════════════════════════════════════════════════════════
        // 10. OPERACIONES EN CURSO
        // ══════════════════════════════════════════════════════════════════════

        // propC2 → en negociacion con diego (abierta)
        operationService.registerOperation(Operation.builder()
                .property(propC2).client(diego).agent(agente2)
                .operationType(OperationType.SALE).value(270_000_000.0)
                .dateInitial(LocalDate.now().minusDays(7)).build());

        // propM2 → reserva de mariela (abierta)
        operationService.registerOperation(Operation.builder()
                .property(propM2).client(mariela).agent(agente3)
                .operationType(OperationType.SALE).value(640_000_000.0)
                .dateInitial(LocalDate.now().minusDays(3)).build());

        // propK3 → negociacion de felipe (abierta)
        operationService.registerOperation(Operation.builder()
                .property(propK3).client(felipe).agent(agente4)
                .operationType(OperationType.SALE).value(680_000_000.0)
                .dateInitial(LocalDate.now().minusDays(2)).build());

        // propK4 → negociacion de arriendo con luis (abierta)
        operationService.registerOperation(Operation.builder()
                .property(propK4).client(luis).agent(agente4)
                .operationType(OperationType.RENT).value(5_200_000.0)
                .dateInitial(LocalDate.now().minusDays(1)).build());

        // ══════════════════════════════════════════════════════════════════════
        // 11. SOLICITUDES DE SOPORTE
        // ══════════════════════════════════════════════════════════════════════

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(sofia).property(propA2).agent(agente1)
                .message("Necesito informacion sobre opciones de financiamiento hipotecario.")
                .date(LocalDateTime.now().minusDays(2)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(tomas).property(propC5).agent(agente2)
                .message("Quiero agendar una nueva visita para negociar el precio.")
                .date(LocalDateTime.now().minusDays(1)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(valentina).property(propS3).agent(agente5)
                .message("Tengo dudas sobre las condiciones del contrato de arriendo.")
                .date(LocalDateTime.now().minusHours(10)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(felipe).property(propK3).agent(agente4)
                .message("Deseo conocer los impuestos prediales y costos notariales.")
                .date(LocalDateTime.now().minusHours(5)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(diego).property(propA2).agent(agente1)
                .message("Es posible visitar en horario de fin de semana?")
                .date(LocalDateTime.now().minusHours(3)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(mariela).property(propM2).agent(agente3)
                .message("Necesito el certificado de tradicion y libertad actualizado.")
                .date(LocalDateTime.now().minusDays(5)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(jorge).property(propK4).agent(agente4)
                .message("Duda sobre los permisos de uso de suelo para local comercial.")
                .date(LocalDateTime.now().minusDays(4)).build());

        agentService.registerSupportRequest(SupportRequest.builder()
                .client(andrea).property(propM3).agent(agente3)
                .message("Incluye parqueadero el apartamento del piso 5?")
                .date(LocalDateTime.now().minusMinutes(45)).build());

        // Atender algunas solicitudes para mantener el flujo
        agentService.attendSupportRequest(agente2.getCedula()); // tomas->C5
        agentService.attendSupportRequest(agente4.getCedula()); // felipe->K3

        // ══════════════════════════════════════════════════════════════════════
        // 12. ALERTA BASICA: CONTRACT_EXPIRING
        //     Operacion tipo CONTRACT_RENEWAL con dateFinal <= 30 dias
        // ══════════════════════════════════════════════════════════════════════

        operationService.registerOperation(Operation.builder()
                .property(propC4).client(tomas).agent(agente2)
                .operationType(OperationType.CONTRACT_RENEWAL).value(2_200_000.0)
                .dateInitial(LocalDate.now().minusMonths(11))
                .dateFinal(LocalDate.now().plusDays(12)).build());   // vence en 12 dias

        operationService.registerOperation(Operation.builder()
                .property(propS2).client(valentina).agent(agente5)
                .operationType(OperationType.CONTRACT_RENEWAL).value(1_700_000.0)
                .dateInitial(LocalDate.now().minusMonths(11))
                .dateFinal(LocalDate.now().plusDays(20)).build());   // vence en 20 dias

        // ══════════════════════════════════════════════════════════════════════
        // 13. ALERTA BASICA: RESERVE_NO_CLOSURE
        //     Operacion CREATED hace > 30 dias sin cerrar
        // ══════════════════════════════════════════════════════════════════════

        operationService.registerOperation(Operation.builder()
                .property(propA5).client(paula).agent(agente1)
                .operationType(OperationType.SALE).value(148_000_000.0)
                .dateInitial(LocalDate.now().minusDays(45)).build()); // 45 dias sin cerrar

        operationService.registerOperation(Operation.builder()
                .property(propS3).client(camila).agent(agente5)
                .operationType(OperationType.RENT).value(1_900_000.0)
                .dateInitial(LocalDate.now().minusDays(38)).build()); // 38 dias sin cerrar

        // ══════════════════════════════════════════════════════════════════════
        // 14. ALERTA BASICA: PROPERTY_NO_VISITS
        //     propA3, propC3, propM1 no tendran visitas — se detectan automaticamente
        //     (ninguna visita registrada para ellos dentro de los ultimos 60 dias)
        // ══════════════════════════════════════════════════════════════════════
        // No se agregan visitas a propA3, propC3 ni propM1 intencionalmente.

        // ══════════════════════════════════════════════════════════════════════
        // 15. ALERTA BASICA: PENDING_VISIT_CONFIRMATION
        //     Visitas PENDING creadas hace > 24h. Registramos sin confirmar.
        // ══════════════════════════════════════════════════════════════════════
        // Las visitas futuras sin confirmVisit() quedan PENDING.
        // vSofiaA2, vDiegoA2, vTomasC5, vMarielaM2, vFelipeK3, vLuisM1 ya fueron
        // CONFIRMED arriba. Ahora agregamos unas sin confirmar (quedaran PENDING):

        visitaSafe(camila, propK3, agente4,
                LocalDateTime.now().plusDays(10), VisitType.NORMAL, null);
        // Queda PENDING — camila no responde — alerta de confirmacion pendiente

        visitaSafe(jorge, propK4, agente4,
                LocalDateTime.now().plusDays(12), VisitType.NORMAL, null);
        // Queda PENDING — dispara PENDING_VISIT_CONFIRMATION cuando createdAt > 24h

        // ══════════════════════════════════════════════════════════════════════
        // 16. ALERTA ANOMALA: HIGH_VISITS_NO_CLOSING
        //     >= 5 visitas COMPLETED en una propiedad sin operacion SALE/RENT.
        //     Usamos propA4 (casa norte Armenia, sin operacion aun).
        //     Cada attendVisit() consume la cabeza de la cola del agente,
        //     así que registramos + confirmamos + atendemos de a una.
        // ══════════════════════════════════════════════════════════════════════

        // 6 visitas COMPLETED en propA4 (diferentes clientes, distintos dias)
        Client[] visitantesA4 = {sofia, tomas, diego, valentina, camila, felipe};
        int[] diasAtrasA4     = {  40,   35,   30,      25,      20,     15    };

        for (int i = 0; i < visitantesA4.length; i++) {
            Visit v = visitaSafe(visitantesA4[i], propA4, agente1,
                    LocalDateTime.now().minusDays(diasAtrasA4[i]), VisitType.NORMAL,
                    "Visita " + (i+1) + " a propA4 — sin cierre");
            if (v != null) {
                visitService.confirmVisit(v);
                agentService.attendVisit(agente1.getCedula());
            }
        }
        // propA4 no tiene operacion — HIGH_VISITS_NO_CLOSING se dispara (6 >= 5)

        // ══════════════════════════════════════════════════════════════════════
        // 17. ALERTA ANOMALA: CLIENT_MULTIPLE_VISITS_NO_CONTINUITY
        //     >= 3 visitas en 30 dias para un cliente sin ninguna COMPLETED.
        //     Usamos camila: visita propK2 (ACTIVE), propC5 (ACTIVE), propM3 (ACTIVE).
        //     NOTA: propK1 es SOLD y propK3 es RESERVED/ACTIVE segun secciones
        //     anteriores, por lo que usamos propiedades definitivamente ACTIVE.
        // ══════════════════════════════════════════════════════════════════════

        // 3 visitas PENDING en los ultimos 25 dias, sin ninguna COMPLETED en la ventana
        visitaSafe(camila, propK2, agente4,
                LocalDateTime.now().minusDays(22), VisitType.NORMAL, null); // PENDING
        visitaSafe(camila, propC5, agente2,
                LocalDateTime.now().minusDays(14), VisitType.NORMAL, null); // PENDING
        visitaSafe(camila, propM3, agente3,
                LocalDateTime.now().minusDays(7),  VisitType.NORMAL, null); // PENDING
        // 3 visitas PENDING en <= 30 dias, ninguna COMPLETED — alerta disparada

        // ══════════════════════════════════════════════════════════════════════
        // 18. ALERTA ANOMALA: AGENT_EXCESSIVE_OVERLOAD
        //     >= 10 visitas PENDING/CONFIRMED para agente1.
        //     Ya tiene: vSofiaA2 (CONFIRMED) + vDiegoA2 (CONFIRMED) = 2
        //     Anadimos 9 visitas PENDING mas a distintos clientes en propA2/propA3/propA4
        // ══════════════════════════════════════════════════════════════════════

        Client[] sobrecargaClientes = {mariela, andrea, jorge, paula, luis,
                tomas, valentina, felipe, camila};
        Property[] sobrecargaProps  = {propA2, propA3, propA4, propA2, propA3,
                propA4, propA2, propA3, propA4};
        for (int i = 0; i < sobrecargaClientes.length; i++) {
            visitaSafe(sobrecargaClientes[i], sobrecargaProps[i], agente1,
                    LocalDateTime.now().plusDays(15 + i), VisitType.NORMAL, null);
        }
        // agente1 acumula > 10 visitas PENDING/CONFIRMED — AGENT_EXCESSIVE_OVERLOAD

        // ══════════════════════════════════════════════════════════════════════
        // 19. ALERTA ANOMALA: PROPERTY_PRICE_CHANGE
        //     >= 3 cambios de precio en 30 dias para una propiedad.
        //     updateProperty() registra PriceHistory cada vez que cambia el precio.
        // ══════════════════════════════════════════════════════════════════════

        // 4 cambios de precio en propC5 en los ultimos 15 dias
        propertyService.updateProperty(Property.builder()
                .code(propC5.getCode()).price(340_000_000.0).build(), false);
        propertyService.updateProperty(Property.builder()
                .code(propC5.getCode()).price(330_000_000.0).build(), false);
        propertyService.updateProperty(Property.builder()
                .code(propC5.getCode()).price(325_000_000.0).build(), false);
        propertyService.updateProperty(Property.builder()
                .code(propC5.getCode()).price(320_000_000.0).build(), false);
        // 4 registros en PriceHistory >= 3 — PROPERTY_PRICE_CHANGE (nivel MEDIUM)

        // ══════════════════════════════════════════════════════════════════════
        // 20. ALERTA ANOMALA: ZONE_INTEREST_CONCENTRATION
        //     >= 5 visitas en 7 dias en la misma zona geografica.
        //     Zona NORTH de Armenia: propA2, propA3, propA4 (todas en barrioElBosque,
        //     que pertenece a Zone.NORTH). Registramos 6 visitas en los ultimos 5 dias.
        // ══════════════════════════════════════════════════════════════════════

        Client[] zonaClientes = {sofia, tomas, diego, mariela, jorge, andrea};
        Property[] zonaProps  = {propA2, propA3, propA4, propA2, propA3, propA4};
        int[]      diasZona   = {   5,     4,     3,      2,      1,      0    };

        for (int i = 0; i < zonaClientes.length; i++) {
            visitaSafe(zonaClientes[i], zonaProps[i], agente1,
                    LocalDateTime.now().minusDays(diasZona[i]).minusHours(2), VisitType.NORMAL, null);
        }
        // 6 visitas en Zone.NORTH en <= 7 dias — ZONE_INTEREST_CONCENTRATION

        // ══════════════════════════════════════════════════════════════════════
        // 21. ALERTA BASICA: HIGH_DEMAND (> 5 visitas en 30 dias en propA2)
        //     propA2 ya acumulo visitas en las secciones anteriores.
        //     Conteo aproximado en los ultimos 30 dias:
        //       - vSofiaA2  (CONFIRMED, +2d)
        //       - vDiegoA2  (CONFIRMED, +3d)
        //       - sobrecarga mariela   (+15d)
        //       - sobrecarga tomas     (+20d)
        //       - sobrecarga valentina (+21d)
        //       - zona sofia (-5d), zona mariela (-2d)
        //     = varios en los ultimos 30 dias — > 5 — HIGH_DEMAND
        // ══════════════════════════════════════════════════════════════════════
        // Cubierto por las visitas ya registradas en propA2.

        System.out.println("DataLoader: datos de prueba cargados correctamente.");
        System.out.println("   Alertas basicas esperadas  : CONTRACT_EXPIRING (x2), PROPERTY_NO_VISITS, HIGH_DEMAND, PENDING_VISIT_CONFIRMATION, RESERVE_NO_CLOSURE (x2), INACTIVE_CLIENT");
        System.out.println("   Alertas anomalas esperadas : HIGH_VISITS_NO_CLOSING, CLIENT_MULTIPLE_VISITS_NO_CONTINUITY, AGENT_EXCESSIVE_OVERLOAD, PROPERTY_PRICE_CHANGE, ZONE_INTEREST_CONCENTRATION");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Helpers privados
    // ══════════════════════════════════════════════════════════════════════════

    /** Registra y publica un inmueble asignandole el agente. */
    private Property publicar(Property property, Agent agente) {
        Property saved = propertyService.registerProperty(property, agente.getCedula(), false);
        agentService.addPropertyToAgent(saved.getCode(), agente.getCedula());
        return propertyService.publishPropertyWithLog(saved.getCode());
    }

    /** Registra una interaccion de cliente con un inmueble. */
    private void interaccion(Client client, Property property,
                             InteractionType type, LocalDateTime timestamp) {
        clientService.registerUserInteraction(UserInteraction.builder()
                .client(client).property(property)
                .interactionType(type).timestamp(timestamp).build());
    }

    /**
     * Registra una visita de forma segura (ignora conflictos de agenda).
     * Devuelve null si no se pudo registrar.
     */
    private Visit visitaSafe(Client client, Property property, Agent agent,
                             LocalDateTime fecha, VisitType tipo, String notas) {
        try {
            Visit v = Visit.builder()
                    .client(client).property(property).agent(agent)
                    .date(fecha).visitType(tipo).postVisitNotes(notas).build();
            Visit saved = agentService.registerVisit(agent, v);
            return saved;
        } catch (RuntimeException e) {
            System.out.println("Visita omitida por conflicto de agenda [" +
                    agent.getName() + " / " + fecha + "]: " + e.getMessage());
            return null;
        }
    }
}