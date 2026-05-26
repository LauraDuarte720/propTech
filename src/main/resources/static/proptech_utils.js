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
    RENTED: 'Arrendado'
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

const ALERT_ABNORMAL_TYPE = {
    HIGH_VISITS_NO_CLOSING: 'Visitas sin cierre',
    CLIENT_MULTIPLE_VISITS_NO_CONTINUITY: 'Cliente sin continuidad',
    AGENT_EXCESSIVE_OVERLOAD: 'Sobrecarga de asesor',
    PROPERTY_PRICE_CHANGE: 'Cambio frecuente de precio',
    ZONE_INTEREST_CONCENTRATION: 'Concentración de interés por zona'
};

const ALERT_TYPE_BASIC = {
    CONTRACT_EXPIRING: 'Contrato próximo a vencer',
    PROPERTY_NO_VISITS: 'Inmueble sin visitas recientes',
    HIGH_DEMAND: 'Alta demanda',
    PENDING_VISIT_CONFIRMATION: 'Visita pendiente de confirmación',
    RESERVE_NO_CLOSURE: 'Reserva sin cierre',
    INACTIVE_CLIENT: 'Cliente inactivo'
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
    PENDINGRESCHEDULE: 'Pendiente de reagendar'
};

const VISIT_TYPE = {
    NORMAL: 'Normal',
    VIP: 'VIP'
};

const CLIENT_TYPE = {
    BUYER: 'Comprador',
    TENANT: 'Arrendatario',
    INVESTOR: 'Inversionista'
};

const SEARCH_STATUS = {
    ACTIVE: 'Buscando activamente',
    PAUSED: 'En pausa',
    NEGOTIATING: 'Negociando',
    CLOSED: 'Búsqueda cerrada',
    INACTIVE: 'Inactivo'
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
    CENTER: 'Centro',
    NORTH: 'Norte',
    SOUTH: 'Sur'
};

const SUPPORT_REQUEST_STATUS = {
    PENDING: 'Pendiente',
    ATTENDED: 'Atendida',
    CANCELLED: 'Cancelada'
};

// Helper para traducir con fallback
function label(map, key) {
    return map[key] || key || '—';
}

// Helper para llenar selects dinámicamente desde los enums
function populateSelect(selectId, enumMap, placeholder) {
    const select = document.getElementById(selectId);
    if (!select) return;
    
    // Guardar el valor actual si lo hay, para restaurarlo después
    const currentValue = select.value;
    
    select.innerHTML = '';
    if (placeholder !== undefined && placeholder !== null) {
        select.innerHTML += `<option value="">${placeholder}</option>`;
    }
    for (const [key, value] of Object.entries(enumMap)) {
        select.innerHTML += `<option value="${key}">${value}</option>`;
    }
    
    // Restaurar el valor si sigue siendo válido
    if (currentValue && Object.keys(enumMap).includes(currentValue)) {
        select.value = currentValue;
    }
}