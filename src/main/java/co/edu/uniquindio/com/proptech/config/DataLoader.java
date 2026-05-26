package co.edu.uniquindio.com.proptech.config;

import co.edu.uniquindio.com.proptech.domain.enums.*;
import co.edu.uniquindio.com.proptech.domain.model.*;
import co.edu.uniquindio.com.proptech.services.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

        // ══════════════════════════════════════════════
        // INMUEBLES EXTRA PARA RECOMENDACIONES
        // ══════════════════════════════════════════════

        // --- Norte Armenia (zona de Sofia y agente1) ---

        Property prop9 = registrarYPublicar(
                Property.builder()
                        .address("Calle 18 # 12-50")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.SALE)
                        .price(290_000_000.0)
                        .area(80.0)
                        .numBedrooms(3)
                        .numBathrooms(2)
                        .build(),
                agente1);

        Property prop10 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 14 # 20-30")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.SALE)
                        .price(255_000_000.0)
                        .area(68.0)
                        .numBedrooms(2)
                        .numBathrooms(2)
                        .build(),
                agente1);

        Property prop11 = registrarYPublicar(
                Property.builder()
                        .address("Calle 22 # 11-15")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(310_000_000.0)
                        .area(95.0)
                        .numBedrooms(3)
                        .numBathrooms(2)
                        .build(),
                agente1);

        // --- Centro Armenia (zona de Sofia y agente2) ---

        Property prop12 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 17 # 9-40")
                        .neighborhood(barrioLaCastellana)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.SALE)
                        .price(275_000_000.0)
                        .area(72.0)
                        .numBedrooms(3)
                        .numBathrooms(2)
                        .build(),
                agente2);

        Property prop13 = registrarYPublicar(
                Property.builder()
                        .address("Calle 12 # 16-22")
                        .neighborhood(barrioLaCastellana)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.RENT)
                        .price(1_500_000.0)
                        .area(60.0)
                        .numBedrooms(2)
                        .numBathrooms(1)
                        .build(),
                agente2);

        // --- Sur Armenia (zona de Valentina y agenteSur) ---

        Property prop14 = registrarYPublicar(
                Property.builder()
                        .address("Avenida Centenario # 5-18")
                        .neighborhood(barrioLaureles)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.RENT)
                        .price(1_700_000.0)
                        .area(58.0)
                        .numBedrooms(2)
                        .numBathrooms(1)
                        .build(),
                agenteSur);

        Property prop15 = registrarYPublicar(
                Property.builder()
                        .address("Calle 3 # 8-55")
                        .neighborhood(barrioLaureles)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.RENT)
                        .price(1_950_000.0)
                        .area(65.0)
                        .numBedrooms(2)
                        .numBathrooms(2)
                        .build(),
                agenteSur);

        // --- Medellín El Poblado (zona de Felipe y agente3) ---

        Property prop16 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 43A # 10-30")
                        .neighborhood(barrioElPoblado)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(650_000_000.0)
                        .area(210.0)
                        .numBedrooms(5)
                        .numBathrooms(4)
                        .build(),
                agente3);

        Property prop17 = registrarYPublicar(
                Property.builder()
                        .address("Calle 8 Sur # 42-80")
                        .neighborhood(barrioElPoblado)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.RENT)
                        .price(4_200_000.0)
                        .area(110.0)
                        .numBedrooms(3)
                        .numBathrooms(3)
                        .build(),
                agente3);

        // --- Cali Ciudad Jardín (zona de Felipe y Camila, agente4) ---

        Property prop18 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 100 # 11-45")
                        .neighborhood(barrioCiudadJardin)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(580_000_000.0)
                        .area(185.0)
                        .numBedrooms(4)
                        .numBathrooms(3)
                        .build(),
                agente4);

        Property prop19 = registrarYPublicar(
                Property.builder()
                        .address("Calle 13 Oeste # 99-20")
                        .neighborhood(barrioCiudadJardin)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.SALE)
                        .price(690_000_000.0)
                        .area(230.0)
                        .numBedrooms(5)
                        .numBathrooms(4)
                        .build(),
                agente4);

        Property prop20 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 98 # 14-60")
                        .neighborhood(barrioCiudadJardin)
                        .propertyType(PropertyType.OFFICE)
                        .purpose(Purpose.RENT)
                        .price(5_500_000.0)
                        .area(120.0)
                        .numBedrooms(0)
                        .numBathrooms(2)
                        .build(),
                agente4);

        // ══════════════════════════════════════════════
        // INMUEBLES EXTRA PARA LLENAR DATOS
        // ══════════════════════════════════════════════
        Property prop21 = registrarYPublicar(
                Property.builder()
                        .address("Avenida El Dorado # 45-12")
                        .neighborhood(barrioElBosque)
                        .propertyType(PropertyType.OFFICE)
                        .purpose(Purpose.RENT)
                        .price(4_000_000.0)
                        .area(150.0)
                        .numBedrooms(0)
                        .numBathrooms(3)
                        .build(),
                agente1);

        Property prop22 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 7 # 12-34")
                        .neighborhood(barrioLaCastellana)
                        .propertyType(PropertyType.APARTMENT)
                        .purpose(Purpose.SALE)
                        .price(350_000_000.0)
                        .area(90.0)
                        .numBedrooms(3)
                        .numBathrooms(2)
                        .build(),
                agente2);

        Property prop23 = registrarYPublicar(
                Property.builder()
                        .address("Calle 100 # 15-20")
                        .neighborhood(barrioElPoblado)
                        .propertyType(PropertyType.HOUSE)
                        .purpose(Purpose.RENT)
                        .price(6_000_000.0)
                        .area(250.0)
                        .numBedrooms(4)
                        .numBathrooms(4)
                        .build(),
                agente3);

        Property prop24 = registrarYPublicar(
                Property.builder()
                        .address("Calle 50 # 10-20")
                        .neighborhood(barrioCiudadJardin)
                        .propertyType(PropertyType.RETAIL_SPACE)
                        .purpose(Purpose.SALE)
                        .price(800_000_000.0)
                        .area(120.0)
                        .numBedrooms(0)
                        .numBathrooms(2)
                        .build(),
                agente4);

        Property prop25 = registrarYPublicar(
                Property.builder()
                        .address("Carrera 5 # 3-10")
                        .neighborhood(barrioLaureles)
                        .propertyType(PropertyType.LOT)
                        .purpose(Purpose.SALE)
                        .price(200_000_000.0)
                        .area(500.0)
                        .numBedrooms(0)
                        .numBathrooms(0)
                        .build(),
                agenteSur);

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

        Client cliente6 = clientService.registerClient(Client.builder()
                .cedula("1093000006")
                .name("Diego Lopez")
                .username("diego.lopez")
                .password("Diego123A")
                .email("diego@email.com")
                .phone("+57300200006")
                .budget(350_000_000.0)
                .minBedrooms(2)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.APARTMENT)
                .build());
        cliente6.addInterestZone(zonaNorteArmenia);

        Client cliente7 = clientService.registerClient(Client.builder()
                .cedula("1093000007")
                .name("Andrea Martinez")
                .username("andrea.martinez")
                .password("Andrea456A")
                .email("andrea@email.com")
                .phone("+57300200007")
                .budget(5_000_000.0)
                .minBedrooms(3)
                .clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.HOUSE)
                .build());
        cliente7.addInterestZone(zonaOrienteMedellin);

        Client cliente8 = clientService.registerClient(Client.builder()
                .cedula("1093000008")
                .name("Jorge Ruiz")
                .username("jorge.ruiz")
                .password("Jorge789A")
                .email("jorge@email.com")
                .phone("+57300200008")
                .budget(900_000_000.0)
                .minBedrooms(4)
                .clientType(ClientType.INVESTOR)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.RETAIL_SPACE)
                .build());
        cliente8.addInterestZone(zonaOccidenteCali);

        Client cliente9 = clientService.registerClient(Client.builder()
                .cedula("1093000009")
                .name("Paula Silva")
                .username("paula.silva")
                .password("Paula321A")
                .email("paula@email.com")
                .phone("+57300200009")
                .budget(250_000_000.0)
                .minBedrooms(0)
                .clientType(ClientType.BUYER)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.LOT)
                .build());
        cliente9.addInterestZone(zonaSurArmenia);

        Client cliente10 = clientService.registerClient(Client.builder()
                .cedula("1093000010")
                .name("Luis Diaz")
                .username("luis.diaz")
                .password("Luis654A")
                .email("luis@email.com")
                .phone("+57300200010")
                .budget(4_500_000.0)
                .minBedrooms(2)
                .clientType(ClientType.TENANT)
                .searchStatus(SearchStatus.ACTIVE)
                .desiredPropertyType(PropertyType.OFFICE)
                .build());
        cliente10.addInterestZone(zonaNorteArmenia);

        // ══════════════════════════════════════════════
        // 6. INTERACCIONES
        // Regla: VISITED solo si hay visita real registrada en sección 7.
        //        Las demás interacciones que antes eran VISITED pasan a CONSULTED.
        // ══════════════════════════════════════════════

        // cliente1 → prop1: tiene visita real (agente1, -9 días) → CONSULTED + SAVED + BUYING_INTENTION
        //            prop3: tiene visita real (agenteSur, +3 días) → CONSULTED
        registerInteraction(cliente1, prop1, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(10));
        registerInteraction(cliente1, prop1, InteractionType.SAVED,              LocalDateTime.now().minusDays(8));
        registerInteraction(cliente1, prop1, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(5));
        registerInteraction(cliente1, prop3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(3));

        // cliente2 → prop2: tiene visita real (agente2, -11 días) → CONSULTED + NEGOTIATED + BUYING_INTENTION
        //            prop5: sin visita real → BUYING_INTENTION se mantiene (no era VISITED)
        registerInteraction(cliente2, prop2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(12));
        registerInteraction(cliente2, prop2, InteractionType.NEGOTIATED,         LocalDateTime.now().minusDays(9));
        registerInteraction(cliente2, prop2, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(6));
        registerInteraction(cliente2, prop5, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(4));

        // cliente3 → prop3: tiene visita real (agenteSur, -6 días) → CONSULTED + RENTING_INTENTION
        registerInteraction(cliente3, prop3, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(7));
        registerInteraction(cliente3, prop3, InteractionType.RENTING_INTENTION,  LocalDateTime.now().minusDays(2));

        // cliente4 → prop5: tiene visita real (agente4, -14 días) → CONSULTED + SAVED + BUYING_INTENTION
        //            prop4: sin visita real → CONSULTED (era CONSULTED, se mantiene)
        registerInteraction(cliente4, prop5, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(15));
        registerInteraction(cliente4, prop5, InteractionType.SAVED,              LocalDateTime.now().minusDays(13));
        registerInteraction(cliente4, prop5, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(8));
        registerInteraction(cliente4, prop4, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(5));

        // cliente5 → prop5: sin visita real → CONSULTED (era VISITED)
        //            prop2: sin visita real → CONSULTED (era VISITED)
        registerInteraction(cliente5, prop5, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(20));
        registerInteraction(cliente5, prop2, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(18));

        // --- Interacciones extra de los últimos 2 meses ---
        registerInteraction(cliente6, prop21, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(55));
        registerInteraction(cliente6, prop22, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(40));
        registerInteraction(cliente6, prop22, InteractionType.SAVED,              LocalDateTime.now().minusDays(38));
        registerInteraction(cliente6, prop22, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(30));

        registerInteraction(cliente7, prop23, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(50));
        registerInteraction(cliente7, prop23, InteractionType.RENTING_INTENTION,  LocalDateTime.now().minusDays(48));

        registerInteraction(cliente8, prop24, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(45));
        registerInteraction(cliente8, prop24, InteractionType.SAVED,              LocalDateTime.now().minusDays(40));
        registerInteraction(cliente8, prop24, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(35));
        registerInteraction(cliente8, prop24, InteractionType.NEGOTIATED,         LocalDateTime.now().minusDays(30));

        registerInteraction(cliente9, prop25, InteractionType.CONSULTED,          LocalDateTime.now().minusDays(28));
        registerInteraction(cliente9, prop25, InteractionType.BUYING_INTENTION,   LocalDateTime.now().minusDays(20));

        registerInteraction(cliente10, prop21, InteractionType.CONSULTED,         LocalDateTime.now().minusDays(15));
        registerInteraction(cliente10, prop21, InteractionType.RENTING_INTENTION, LocalDateTime.now().minusDays(10));

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

        // --- Visitas extra de los últimos 2 meses ---
        registerVisitSafe(Visit.builder()
                .client(cliente6).property(prop22).agent(agente2)
                .date(LocalDateTime.now().minusDays(32))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("Muy interesado en el apartamento, revisará con el banco.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente7).property(prop23).agent(agente3)
                .date(LocalDateTime.now().minusDays(49))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("Le encantó la casa, lista para rentar.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente8).property(prop24).agent(agente4)
                .date(LocalDateTime.now().minusDays(38))
                .visitType(VisitType.VIP)
                .postVisitNotes("Evalúa el local para una franquicia.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente8).property(prop24).agent(agente4)
                .date(LocalDateTime.now().minusDays(32))
                .visitType(VisitType.VIP)
                .postVisitNotes("Segunda visita con arquitecto. Va a negociar.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente9).property(prop25).agent(agenteSur)
                .date(LocalDateTime.now().minusDays(25))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("El lote es ideal para su proyecto.")
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente10).property(prop21).agent(agente1)
                .date(LocalDateTime.now().minusDays(12))
                .visitType(VisitType.NORMAL)
                .postVisitNotes("Le gustó la oficina, preguntará por adecuaciones.")
                .build());

        // Agendadas a futuro
        registerVisitSafe(Visit.builder()
                .client(cliente6).property(prop9).agent(agente1)
                .date(LocalDateTime.now().plusDays(2))
                .visitType(VisitType.NORMAL)
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente7).property(prop17).agent(agente3)
                .date(LocalDateTime.now().plusDays(5))
                .visitType(VisitType.NORMAL)
                .build());
        
        registerVisitSafe(Visit.builder()
                .client(cliente9).property(prop6).agent(agente1)
                .date(LocalDateTime.now().plusDays(7))
                .visitType(VisitType.VIP)
                .build());

        registerVisitSafe(Visit.builder()
                .client(cliente10).property(prop4).agent(agente3)
                .date(LocalDateTime.now().plusDays(10))
                .visitType(VisitType.NORMAL)
                .build());

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

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente6)
                        .property(prop22)
                        .agent(agente2)
                        .message("Necesito ayuda con el certificado de tradición y libertad.")
                        .date(LocalDateTime.now().minusDays(28))
                        .build()
        );

        agentService.registerSupportRequest(
                SupportRequest.builder()
                        .client(cliente8)
                        .property(prop24)
                        .agent(agente4)
                        .message("Duda sobre los permisos de uso de suelo.")
                        .date(LocalDateTime.now().minusDays(30))
                        .build()
        );

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
                .operationType(OperationType.CONTRACT_RENEWAL)
                .value(2_200_000.0)
                .dateInitial(LocalDate.now().minusMonths(11))
                .dateFinal(LocalDate.now().plusDays(15))
                .build());

        // --- Operaciones extra de los últimos 2 meses ---
        Operation opExtra1 = operationService.registerOperation(Operation.builder()
                .property(prop22)
                .client(cliente6)
                .agent(agente2)
                .operationType(OperationType.SALE)
                .value(345_000_000.0)
                .dateInitial(LocalDate.now().minusDays(25))
                .dateFinal(LocalDate.now().minusDays(5))
                .build());
        operationService.updateOperation(Operation.builder()
                .id(opExtra1.getId())
                .processStatus(ProcessStatus.CLOSED)
                .build());

        Operation opExtra2 = operationService.registerOperation(Operation.builder()
                .property(prop23)
                .client(cliente7)
                .agent(agente3)
                .operationType(OperationType.RENT)
                .value(6_000_000.0)
                .dateInitial(LocalDate.now().minusDays(45))
                .dateFinal(LocalDate.now().plusMonths(12))
                .build());
        operationService.updateOperation(Operation.builder()
                .id(opExtra2.getId())
                .processStatus(ProcessStatus.CLOSED)
                .build());

        Operation opExtra3 = operationService.registerOperation(Operation.builder()
                .property(prop24)
                .client(cliente8)
                .agent(agente4)
                .operationType(OperationType.SALE)
                .value(780_000_000.0)
                .dateInitial(LocalDate.now().minusDays(28))
                .build());

        Operation opExtra4 = operationService.registerOperation(Operation.builder()
                .property(prop25)
                .client(cliente9)
                .agent(agenteSur)
                .operationType(OperationType.SALE)
                .value(195_000_000.0)
                .dateInitial(LocalDate.now().minusDays(18))
                .dateFinal(LocalDate.now().minusDays(2))
                .build());
        operationService.updateOperation(Operation.builder()
                .id(opExtra4.getId())
                .processStatus(ProcessStatus.CLOSED)
                .build());

        Operation opExtra5 = operationService.registerOperation(Operation.builder()
                .property(prop21)
                .client(cliente10)
                .agent(agente1)
                .operationType(OperationType.RENT)
                .value(3_900_000.0)
                .dateInitial(LocalDate.now().minusDays(8))
                .build());

        // ─── RESERVE_NO_CLOSURE ─────────────────────────────────────────────
        operationService.registerOperation(Operation.builder()
                .property(prop6)
                .client(cliente4)
                .agent(agente1)
                .operationType(OperationType.SALE)
                .value(150_000_000.0)
                .dateInitial(LocalDate.now().minusDays(35))
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

        // Atender solicitudes
        agentService.attendSupportRequest(agente2.getCedula());
        agentService.attendSupportRequest(agente4.getCedula());

        // Cancelar solicitud
        SupportRequest pending = agentService.getNextSupportRequest(agente1.getCedula());
        if (pending != null) {
            agentService.cancelSupportRequest(agente1.getCedula(), pending.getId());
        }

        generateMassiveData(
                agente1, agente2, agente3, agente4, agenteSur,
                barrioElBosque, barrioLaCastellana, barrioLaureles, barrioElPoblado, barrioCiudadJardin,
                zonaNorteArmenia, zonaCentroArmenia, zonaSurArmenia, zonaOrienteMedellin, zonaOccidenteCali
        );

        System.out.println("✅ DataLoader: datos de prueba cargados correctamente.");

        // ══════════════════════════════════════════════
        // 9. DATOS EXTRA PARA DISPARAR ALERTAS ANÓMALAS
        // ══════════════════════════════════════════════

        // HIGH_VISITS_NO_CLOSING: prop6 necesita 5+ visitas COMPLETED sin operación
        for (int i = 0; i < 6; i++) {
            Client c = (i % 2 == 0) ? cliente1 : cliente3;
            registerVisitSafe(Visit.builder()
                    .client(c).property(prop6).agent(agente1)
                    .date(LocalDateTime.now().minusDays(20 + i))
                    .visitType(VisitType.NORMAL)
                    .postVisitNotes("Visita " + (i+1) + " sin cierre")
                    .build());
        }

        // CLIENT_MULTIPLE_VISITS_NO_CONTINUITY: cliente5 hace visitas en 30 días sin COMPLETED
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

    private void generateMassiveData(
            Agent agente1, Agent agente2, Agent agente3, Agent agente4, Agent agenteSur,
            Neighborhood bNorte, Neighborhood bCentro, Neighborhood bSur, Neighborhood bPoblado, Neighborhood bCiudadJardin,
            GeographicZone zNorte, GeographicZone zCentro, GeographicZone zSur, GeographicZone zPoblado, GeographicZone zCiudadJardin
    ) {
        Random r = new Random(12345);
        String[] names = {"Alejandro", "Maria", "Juan", "Ana", "Carlos", "Luis", "Marta", "Lucia", "Jose", "Pedro", "Sofia", "Diego", "Valentina", "Andres", "Camila", "Mateo", "Isabella", "Daniel", "Valeria", "David"};
        String[] surnames = {"Gomez", "Perez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Fernandez", "Gonzalez", "Diaz", "Torres", "Ramirez", "Ruiz", "Vargas", "Castro", "Ortiz"};

        Agent[] agents = {agente1, agente2, agenteSur, agente3, agente4};
        Neighborhood[] neighborhoods = {bNorte, bCentro, bSur, bPoblado, bCiudadJardin};
        GeographicZone[] zones = {zNorte, zCentro, zSur, zPoblado, zCiudadJardin};

        List<Property> generatedProperties = new ArrayList<>();
        // 1. Generate 30 Properties
        for (int i = 0; i < 30; i++) {
            int agentIndex = r.nextInt(agents.length);
            Agent agent = agents[agentIndex];
            Neighborhood neighborhood = neighborhoods[agentIndex]; 
            
            PropertyType type = PropertyType.values()[r.nextInt(PropertyType.values().length)];
            Purpose purpose = Purpose.values()[r.nextInt(Purpose.values().length)];
            
            double price = 100_000_000.0 + r.nextInt(900_000_000);
            if (purpose == Purpose.RENT) {
                price = 1_000_000.0 + r.nextInt(9_000_000);
            }
            
            Property p = Property.builder()
                    .address("Gen-Calle " + (r.nextInt(100)+1) + " # " + (r.nextInt(100)+1) + "-" + r.nextInt(100))
                    .neighborhood(neighborhood)
                    .propertyType(type)
                    .purpose(purpose)
                    .price(price)
                    .area(40.0 + r.nextInt(200))
                    .numBedrooms(type == PropertyType.LOT || type == PropertyType.RETAIL_SPACE || type == PropertyType.WAREHOUSE ? 0 : 1 + r.nextInt(4))
                    .numBathrooms(type == PropertyType.LOT ? 0 : 1 + r.nextInt(3))
                    .build();
                    
            generatedProperties.add(registrarYPublicar(p, agent));
        }
        
        List<Client> generatedClients = new ArrayList<>();
        // 2. Generate 50 Clients
        for (int i = 0; i < 50; i++) {
            String name = names[r.nextInt(names.length)] + " " + surnames[r.nextInt(surnames.length)];
            ClientType cType = ClientType.values()[r.nextInt(ClientType.values().length)];
            PropertyType dType = PropertyType.values()[r.nextInt(PropertyType.values().length)];
            
            double budget = 150_000_000.0 + r.nextInt(1_000_000_000);
            if (cType == ClientType.TENANT) budget = 1_500_000.0 + r.nextInt(10_000_000);
            
            Client c = Client.builder()
                    .cedula("1095000" + String.format("%03d", i))
                    .name(name)
                    .username("genuser" + i)
                    .password("Pass123A")
                    .email("genuser" + i + "@email.com")
                    .phone("+57300300" + String.format("%04d", i))
                    .budget(budget)
                    .minBedrooms(dType == PropertyType.LOT ? 0 : 1 + r.nextInt(3))
                    .clientType(cType)
                    .searchStatus(SearchStatus.ACTIVE)
                    .desiredPropertyType(dType)
                    .build();
                    
            c = clientService.registerClient(c);
            c.addInterestZone(zones[r.nextInt(zones.length)]);
            generatedClients.add(c);
        }
        
        // 3. Generate Interactions, Visits, Support Requests and Operations consistently
        for (Client c : generatedClients) {
            int numInteractions = 2 + r.nextInt(5); // 2 to 6 interactions per client
            for (int j = 0; j < numInteractions; j++) {
                Property p = generatedProperties.get(r.nextInt(generatedProperties.size()));
                
                // Find agent for property (from neighborhood match)
                Agent pAgent = null;
                for(int k=0; k < neighborhoods.length; k++) {
                    if(p.getNeighborhood().getId().equals(neighborhoods[k].getId())) {
                        pAgent = agents[k];
                        break;
                    }
                }
                if (pAgent == null) pAgent = agente1; // fallback
                
                int daysAgo = 1 + r.nextInt(60);
                LocalDateTime interactionDate = LocalDateTime.now().minusDays(daysAgo);
                
                registerInteraction(c, p, InteractionType.CONSULTED, interactionDate);
                
                boolean visited = r.nextDouble() < 0.6;
                if (visited) {
                    LocalDateTime visitDate = interactionDate.plusDays(1 + r.nextInt(3));
                    VisitType vType = r.nextDouble() < 0.2 ? VisitType.VIP : VisitType.NORMAL;
                    
                    if (visitDate.isBefore(LocalDateTime.now())) {
                        registerVisitSafe(Visit.builder()
                                .client(c).property(p).agent(pAgent)
                                .date(visitDate)
                                .visitType(vType)
                                .postVisitNotes(r.nextBoolean() ? "Cliente muy interesado." : "Visita completada. No se convenció.")
                                .build());
                        
                        InteractionType intent = p.getPurpose() == Purpose.RENT ? InteractionType.RENTING_INTENTION : InteractionType.BUYING_INTENTION;
                        registerInteraction(c, p, intent, visitDate.plusHours(2));

                        if (r.nextDouble() < 0.15) {

                            if (p.getStatus() == PropertyStatus.ACTIVE) {

                                agentService.registerSupportRequest(
                                        SupportRequest.builder()
                                                .client(c)
                                                .property(p)
                                                .agent(pAgent)
                                                .message("Solicito más información técnica y financiera sobre la propiedad.")
                                                .date(visitDate.plusDays(1))
                                                .build());
                            }
                        }

                        if (r.nextDouble() < 0.1) {

                            if (p.getStatus() != PropertyStatus.ACTIVE) {
                                continue;
                            }

                            OperationType opType = p.getPurpose() == Purpose.RENT
                                    ? OperationType.RENT
                                    : OperationType.SALE;

                            double val = p.getPrice() * (0.9 + (r.nextDouble() * 0.1));

                            Operation op = operationService.registerOperation(Operation.builder()
                                    .property(p)
                                    .client(c)
                                    .agent(pAgent)
                                    .operationType(opType)
                                    .value(val)
                                    .dateInitial(visitDate.toLocalDate().plusDays(2))
                                    .build());

                            if (r.nextDouble() < 0.5) {
                                operationService.updateOperation(Operation.builder()
                                        .id(op.getId())
                                        .processStatus(ProcessStatus.CLOSED)
                                        .build());
                            }
                        }
                    } else {
                        registerVisitSafe(Visit.builder()
                                .client(c).property(p).agent(pAgent)
                                .date(visitDate)
                                .visitType(vType)
                                .build());
                    }
                } else {
                    if (r.nextBoolean()) {
                        registerInteraction(c, p, InteractionType.SAVED, interactionDate.plusDays(1));
                    }
                }
            }
        }
    }

    // ── helpers privados ──────────────────────────────────────────────────────

    private Property registrarYPublicar(Property property, Agent agente) {
        Property saved = propertyService.registerProperty(property, agente.getCedula(), false);
        agentService.addPropertyToAgent(saved.getCode(), agente.getCedula());
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
            agentService.registerVisit(visit.getAgent(), visit);
        } catch (RuntimeException e) {
            System.out.println("⚠️  Visita omitida por conflicto de agenda: " + e.getMessage());
        }
    }
}