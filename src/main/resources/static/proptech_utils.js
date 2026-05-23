const PROPERTY_TYPE = {
    APARTMENT: 'Apartamento',
    HOUSE: 'Casa',
    RETAIL_SPACE: 'Local comercial',
    OFFICE: 'Oficina',
    LOT: 'Lote',
    WAREHOUSE: 'Bodega'
};

const PROPERTY_STATUS = {
    ACTIVE: 'Activo',
    NEW: 'Nuevo',
    INACTIVE: 'Inactivo',
    RESERVED: 'Reservado',
    SOLD: 'Vendido',
    RENTED: 'Arrendado',
    UNDER_NEGOTIATION: 'En negociación'
};

const PROPERTY_PURPOSE = {
    SALE: 'Venta',
    RENT: 'Arriendo'
};

const OPERATION_TYPE = {
    SALE: 'Venta',
    RENT: 'Arriendo',
    CONTRACT_RENEWAL: 'Renovación de contrato',
    DEAL_CANCELLATION: 'Cancelación'
};

const PROCESS_STATUS = {
    CREATED: 'Creado',
    CLOSED: 'Cerrado',
    CANCELLED: 'Cancelado'
};

const ALERT_TYPE = {
    HIGH_VISITS_NO_CLOSING: 'Visitas sin cierre',
    CLIENT_MULTIPLE_VISITS_NO_CONTINUITY: 'Cliente sin continuidad',
    AGENT_EXCESSIVE_OVERLOAD: 'Sobrecarga de asesor',
    PROPERTY_PRICE_CHANGE: 'Cambio frecuente de precio',
    ZONE_INTEREST_CONCENTRATION: 'Concentración de interés por zona'
};

const ATTENTION_LEVEL = {
    HIGH: 'Alto',
    MEDIUM: 'Medio',
    LOW: 'Bajo'
};

const CITY = {
    ARAUCA: 'Arauca',
    ARMENIA: 'Armenia',
    BARRANQUILLA: 'Barranquilla',
    BOGOTA: 'Bogota',
    BUCARAMANGA: 'Bucaramanga',
    CALI: 'Cali',
    CARTAGENA: 'Cartagena',
    CUCUTA: 'Cucuta',
    FLORENCIA: 'Florencia',
    IBAGUE: 'Ibague',
    INIRIDA: 'Inirida',
    LETICIA: 'Leticia',
    MANIZALES: 'Manizales',
    MEDELLIN: 'Medellin',
    MITU: 'Mitu',
    MOCOA: 'Mocoa',
    MONTERIA: 'Monteria',
    NEIVA: 'Neiva',
    PASTO: 'Pasto',
    PEREIRA: 'Pereira',
    POPAYAN: 'Popayan',
    PUERTO_CARRENO: 'Puerto Carreno',
    QUIBDO: 'Quibdo',
    RIOHACHA: 'Riohacha',
    SAN_ANDRES: 'San Andres',
    SAN_JOSE_DEL_GUAVIARE: 'San Jose del Guaviare',
    SANTA_MARTA: 'Santa Marta',
    SINCELEJO: 'Sincelejo',
    TUNJA: 'Tunja',
    VALLEDUPAR: 'Valledupar',
    VILLAVICENCIO: 'Villavicencio',
    YOPAL: 'Yopal'
};

const VISIT_STATUS = {
    PENDING: 'Pendiente',
    CONFIRMED: 'Confirmada',
    COMPLETED: 'Completada',
    CANCELED: 'Cancelada',
    RESCHEDULED: 'Reagendada',
    PENDINGRESCHEDULE: 'Pendiente de reagendar',
    VIP: 'VIP'
};

const VISIT_TYPE = {
    NORMAL: 'Normal',
    VIP: 'VIP'
};

const CLIENT_TYPE = {
    BUYER: 'Comprador',
    RENTER: 'Arrendatario',
    INVESTOR: 'Inversionista'
};

const SEARCH_STATUS = {
    ACTIVE: 'Buscando activamente',
    PAUSED: 'En pausa',
    CLOSED: 'Búsqueda cerrada'
};

const INTERACTION_TYPE = {
    CONSULTED: 'Consultó',
    VISITED: 'Visitó',
    SAVED: 'Guardó',
    DISCARDED: 'Descartó',
    NEGOTIATED: 'Negoció',
    BUYING_INTENTION: 'Intención de compra',
    RENTING_INTENTION: 'Intención de arriendo'
};

const ZONE = {
    NORTE: 'Norte',
    SUR: 'Sur',
    ORIENTE: 'Oriente',
    OCCIDENTE: 'Occidente',
    CENTRO: 'Centro'
};

// Helper para traducir con fallback
function label(map, key) {
    return map[key] || key || '—';
}