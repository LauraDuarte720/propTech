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
    PENDINGRESCHEDULE: 'Pendiente de reagendar',
    EXPIRED: 'Vencida'
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

/* ════════════════════════════════════════════════════════════════════
   UI COMPARTIDA: toasts, modales de confirmación e historial de cambios
   Prefijo pt- para no chocar con los estilos de cada página.
   Todas las pantallas cargan proptech_utils.js, así que estas funciones
   quedan disponibles en cualquier vista del admin.
════════════════════════════════════════════════════════════════════ */

// Base del API. Las páginas definen `const API='http://localhost:8080'`;
// pueden exponerla con  window.PT_API = API;  si no, se asume localhost.
function ptApi() {
    if (window.PT_API) return window.PT_API;
    if (location && location.origin && location.origin.startsWith('http')) return location.origin;
    return 'http://localhost:8080';
}

let _ptUiReady = false;
function ptEnsureUI() {
    if (_ptUiReady) return;
    _ptUiReady = true;

    const style = document.createElement('style');
    style.textContent = `
    .pt-overlay{position:fixed;inset:0;z-index:2147483000;display:flex;align-items:center;justify-content:center;
      background:rgba(6,8,20,0.72);backdrop-filter:blur(6px);-webkit-backdrop-filter:blur(6px);
      opacity:0;transition:opacity .18s ease;padding:20px;}
    .pt-overlay.pt-show{opacity:1;}
    .pt-card{width:440px;max-width:100%;background:#131936;border:1px solid rgba(255,255,255,0.10);
      border-radius:18px;padding:26px 26px 22px;box-shadow:0 24px 70px rgba(0,0,0,0.55);
      font-family:'Outfit','Jost',system-ui,sans-serif;color:#fff;transform:translateY(12px) scale(.98);
      transition:transform .2s cubic-bezier(.2,.8,.2,1);}
    .pt-overlay.pt-show .pt-card{transform:translateY(0) scale(1);}
    .pt-ico{width:52px;height:52px;border-radius:14px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;}
    .pt-ico svg{width:26px;height:26px;}
    .pt-ico.warning{background:rgba(239,159,39,0.14);color:#EF9F27;box-shadow:inset 0 0 0 1px rgba(239,159,39,0.25);}
    .pt-ico.danger{background:rgba(226,75,74,0.14);color:#FF7B7B;box-shadow:inset 0 0 0 1px rgba(226,75,74,0.28);}
    .pt-ico.info{background:rgba(74,111,227,0.16);color:#6B8AFF;box-shadow:inset 0 0 0 1px rgba(74,111,227,0.3);}
    .pt-ico.success{background:rgba(29,158,117,0.16);color:#3DD68C;box-shadow:inset 0 0 0 1px rgba(29,158,117,0.3);}
    .pt-title{font-size:18px;font-weight:600;margin-bottom:8px;letter-spacing:-0.2px;}
    .pt-msg{font-size:13.5px;line-height:1.55;color:#C4C9DA;}
    .pt-detail{margin-top:14px;font-size:12.5px;line-height:1.5;color:#EF9F27;background:rgba(239,159,39,0.08);
      border:1px solid rgba(239,159,39,0.2);border-radius:10px;padding:11px 13px;}
    .pt-detail.danger{color:#FF9B9B;background:rgba(226,75,74,0.08);border-color:rgba(226,75,74,0.22);}
    .pt-actions{display:flex;justify-content:flex-end;gap:10px;margin-top:24px;}
    .pt-btn{padding:9px 18px;border-radius:10px;font-family:inherit;font-size:13px;font-weight:600;
      cursor:pointer;border:none;transition:filter .15s, background .15s, transform .05s;}
    .pt-btn:active{transform:translateY(1px);}
    .pt-btn-ghost{background:rgba(255,255,255,0.07);color:#fff;border:1px solid rgba(255,255,255,0.12);}
    .pt-btn-ghost:hover{background:rgba(255,255,255,0.12);}
    .pt-btn-warning{background:#EF9F27;color:#1a1304;}
    .pt-btn-warning:hover{filter:brightness(1.07);}
    .pt-btn-danger{background:#E24B4A;color:#fff;}
    .pt-btn-danger:hover{filter:brightness(1.08);}
    .pt-btn-info{background:#4A6FE3;color:#fff;}
    .pt-btn-info:hover{filter:brightness(1.1);}

    /* Toasts */
    #pt-toast{position:fixed;bottom:26px;right:26px;z-index:2147483600;display:flex;flex-direction:column;gap:10px;pointer-events:none;}
    .pt-toast-item{display:flex;align-items:flex-start;gap:12px;padding:14px 18px;border-radius:12px;font-size:13px;
      max-width:380px;pointer-events:all;box-shadow:0 12px 36px rgba(0,0,0,0.45);
      font-family:'Outfit',system-ui,sans-serif;animation:ptIn .26s cubic-bezier(.2,.8,.2,1);}
    .pt-toast-item.error{background:#1e1016;border:1px solid rgba(226,75,74,0.4);color:#FF8E8E;}
    .pt-toast-item.success{background:#0e1a15;border:1px solid rgba(29,158,117,0.4);color:#46DF97;}
    .pt-toast-item.info{background:#0f1428;border:1px solid rgba(74,111,227,0.4);color:#7E9BFF;}
    .pt-toast-item.warning{background:#1d1707;border:1px solid rgba(239,159,39,0.4);color:#F4B24A;}
    .pt-toast-ic{font-size:16px;line-height:1.2;flex-shrink:0;}
    .pt-toast-tt{font-weight:700;}
    .pt-toast-ms{font-size:12px;opacity:.85;line-height:1.45;margin-top:2px;}
    @keyframes ptIn{from{opacity:0;transform:translateX(16px)}to{opacity:1;transform:translateX(0)}}
    @keyframes ptOut{to{opacity:0;transform:translateX(16px)}}

    /* Historial */
    .pt-hist-card{width:680px;max-width:100%;max-height:82vh;display:flex;flex-direction:column;padding:0;overflow:hidden;}
    .pt-hist-head{padding:22px 24px 16px;border-bottom:1px solid rgba(255,255,255,0.08);}
    .pt-hist-head h3{font-size:18px;font-weight:600;display:flex;align-items:center;gap:10px;}
    .pt-hist-head p{font-size:12px;color:#8A90A0;margin-top:6px;line-height:1.5;}
    .pt-hist-x{margin-left:auto;background:none;border:none;color:#8A90A0;font-size:22px;cursor:pointer;line-height:1;}
    .pt-hist-x:hover{color:#fff;}
    .pt-hist-filters{display:flex;gap:8px;padding:14px 24px 4px;flex-wrap:wrap;}
    .pt-chip{padding:5px 13px;border-radius:99px;font-size:12px;font-family:'Jost',sans-serif;cursor:pointer;
      background:rgba(255,255,255,0.05);border:1px solid rgba(255,255,255,0.1);color:#8A90A0;transition:all .15s;}
    .pt-chip:hover{color:#fff;}
    .pt-chip.on{background:rgba(74,111,227,0.18);border-color:rgba(74,111,227,0.5);color:#fff;}
    .pt-hist-list{overflow-y:auto;padding:12px 16px 18px;flex:1;}
    .pt-hist-row{display:flex;gap:13px;padding:13px 12px;border-radius:11px;transition:background .12s;}
    .pt-hist-row:hover{background:rgba(255,255,255,0.03);}
    .pt-hist-row + .pt-hist-row{border-top:1px solid rgba(255,255,255,0.05);}
    .pt-hist-dot{width:34px;height:34px;border-radius:10px;display:flex;align-items:center;justify-content:center;flex-shrink:0;}
    .pt-hist-dot svg{width:17px;height:17px;}
    .pt-act-CREATE{background:rgba(74,111,227,0.15);color:#6B8AFF;}
    .pt-act-UPDATE{background:rgba(239,159,39,0.15);color:#EF9F27;}
    .pt-act-PUBLISH{background:rgba(29,158,117,0.15);color:#3DD68C;}
    .pt-act-UNPUBLISH{background:rgba(138,144,160,0.18);color:#A7AEC2;}
    .pt-hist-main{flex:1;min-width:0;}
    .pt-hist-desc{font-size:13px;line-height:1.45;color:#E6E9F2;}
    .pt-hist-meta{font-size:11px;color:#8A90A0;margin-top:4px;font-family:'Jost',sans-serif;
      display:flex;gap:8px;flex-wrap:wrap;align-items:center;}
    .pt-hist-badge{font-size:10px;padding:2px 8px;border-radius:99px;font-weight:600;letter-spacing:.3px;}
    .pt-badge-undo{background:rgba(29,158,117,0.14);color:#3DD68C;}
    .pt-badge-locked{background:rgba(226,75,74,0.14);color:#FF8E8E;}
    .pt-hist-empty{text-align:center;color:#8A90A0;padding:48px 20px;font-size:13px;}
    .pt-hist-foot{padding:13px 24px;border-top:1px solid rgba(255,255,255,0.08);font-size:11.5px;color:#8A90A0;
      display:flex;align-items:center;gap:8px;}
  `;
    document.head.appendChild(style);

    const toast = document.createElement('div');
    toast.id = 'pt-toast';
    document.body.appendChild(toast);
}

const PT_ICONS = {
    warning: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M12 9v4M12 17h.01M10.3 3.86l-8.5 14.74A2 2 0 0 0 3.53 21.6h16.94a2 2 0 0 0 1.73-3L13.7 3.86a2 2 0 0 0-3.4 0z"/></svg>',
    danger:  '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2m3 0v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/></svg>',
    info:    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/></svg>',
    success: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M20 6L9 17l-5-5"/></svg>'
};

// Toast bonito. type: error | success | info | warning
function ptToast(type, title, msg) {
    ptEnsureUI();
    const icons = { error:'✕', success:'✓', info:'ℹ', warning:'⚠' };
    const el = document.createElement('div');
    el.className = `pt-toast-item ${type}`;
    el.innerHTML = `<div class="pt-toast-ic">${icons[type]||'ℹ'}</div>
    <div><div class="pt-toast-tt">${ptEsc(title)}</div>${msg?`<div class="pt-toast-ms">${ptEsc(msg)}</div>`:''}</div>`;
    document.getElementById('pt-toast').appendChild(el);
    setTimeout(() => { el.style.animation = 'ptOut .3s ease forwards'; setTimeout(()=>el.remove(), 300); }, 4200);
}

// Modal de confirmación bonito -> devuelve Promise<boolean>
// opts: { title, message, detail, confirmText, cancelText, tone: 'warning'|'danger'|'info' }
function ptConfirm(opts) {
    ptEnsureUI();
    const o = Object.assign({ title:'¿Confirmar?', message:'', detail:'', confirmText:'Confirmar', cancelText:'Cancelar', tone:'warning' }, opts || {});
    return new Promise(resolve => {
        const overlay = document.createElement('div');
        overlay.className = 'pt-overlay';
        const btnTone = o.tone === 'danger' ? 'pt-btn-danger' : (o.tone === 'info' ? 'pt-btn-info' : 'pt-btn-warning');
        overlay.innerHTML = `
      <div class="pt-card" role="dialog" aria-modal="true">
        <div class="pt-ico ${o.tone}">${PT_ICONS[o.tone]||PT_ICONS.warning}</div>
        <div class="pt-title">${ptEsc(o.title)}</div>
        <div class="pt-msg">${ptEsc(o.message)}</div>
        ${o.detail ? `<div class="pt-detail ${o.tone==='danger'?'danger':''}">${ptEsc(o.detail)}</div>` : ''}
        <div class="pt-actions">
          <button class="pt-btn pt-btn-ghost" data-pt="cancel">${ptEsc(o.cancelText)}</button>
          <button class="pt-btn ${btnTone}" data-pt="ok">${ptEsc(o.confirmText)}</button>
        </div>
      </div>`;
        document.body.appendChild(overlay);
        requestAnimationFrame(() => overlay.classList.add('pt-show'));

        const close = (val) => {
            overlay.classList.remove('pt-show');
            setTimeout(() => overlay.remove(), 180);
            document.removeEventListener('keydown', onKey);
            resolve(val);
        };
        const onKey = (e) => { if (e.key === 'Escape') close(false); if (e.key === 'Enter') close(true); };
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) close(false);
            const act = e.target.closest('[data-pt]');
            if (act) close(act.dataset.pt === 'ok');
        });
        document.addEventListener('keydown', onKey);
        setTimeout(() => overlay.querySelector('[data-pt="ok"]').focus(), 60);
    });
}

const PT_ACT_LABEL = { CREATE:'Creación', UPDATE:'Actualización', PUBLISH:'Publicación', UNPUBLISH:'Despublicación' };
const PT_ENT_LABEL = { PROPERTY:'Inmueble', AGENT:'Asesor' };
const PT_ACT_ICON = {
    CREATE:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M12 5v14M5 12h14"/></svg>',
    UPDATE:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4z"/></svg>',
    PUBLISH:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M22 2L11 13M22 2l-7 20-4-9-9-4z"/></svg>',
    UNPUBLISH:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M9.88 9.88a3 3 0 1 0 4.24 4.24M10.7 5.08A10.4 10.4 0 0 1 12 5c7 0 10 7 10 7a13.2 13.2 0 0 1-1.67 2.68M6.06 6.06A13.4 13.4 0 0 0 2 12s3 7 10 7a9.7 9.7 0 0 0 5.94-2.06M2 2l20 20"/></svg>'
};

let _ptHistFilter = 'ALL';
let _ptHistData = [];

// Abre el modal con el historial completo de acciones administrativas.
async function ptHistory() {
    ptEnsureUI();
    const overlay = document.createElement('div');
    overlay.className = 'pt-overlay';
    overlay.id = 'pt-hist-overlay';
    overlay.innerHTML = `
    <div class="pt-card pt-hist-card">
      <div class="pt-hist-head">
        <h3>
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" style="width:20px;height:20px;color:#6B8AFF"><path d="M3 3v5h5"/><path d="M3.05 13A9 9 0 1 0 6 5.3L3 8"/><path d="M12 7v5l4 2"/></svg>
          Historial de cambios
          <button class="pt-hist-x" data-pt="close">×</button>
        </h3>
        <p>Registro completo de acciones (cola). La pila de deshacer guarda solo las <b>últimas 15 acciones deshacibles</b>; los cambios de barrio o asesor sobre inmuebles con visitas u operaciones quedan marcados como no deshacibles.</p>
      </div>
      <div class="pt-hist-filters">
        <button class="pt-chip on" data-filter="ALL">Todas</button>
        <button class="pt-chip" data-filter="PROPERTY">Inmuebles</button>
        <button class="pt-chip" data-filter="AGENT">Asesores</button>
        <button class="pt-chip" data-filter="UNDO">Deshacibles</button>
      </div>
      <div class="pt-hist-list" id="pt-hist-list">
        <div class="pt-hist-empty">Cargando historial…</div>
      </div>
      <div class="pt-hist-foot">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" style="width:14px;height:14px"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/></svg>
        <span id="pt-hist-count">—</span>
      </div>
    </div>`;
    document.body.appendChild(overlay);
    requestAnimationFrame(() => overlay.classList.add('pt-show'));

    const close = () => { overlay.classList.remove('pt-show'); setTimeout(()=>overlay.remove(),180); document.removeEventListener('keydown', onKey); };
    const onKey = (e) => { if (e.key === 'Escape') close(); };
    document.addEventListener('keydown', onKey);
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay || e.target.closest('[data-pt="close"]')) { close(); return; }
        const chip = e.target.closest('[data-filter]');
        if (chip) {
            _ptHistFilter = chip.dataset.filter;
            overlay.querySelectorAll('.pt-chip').forEach(c => c.classList.toggle('on', c === chip));
            ptRenderHist();
        }
    });

    _ptHistFilter = 'ALL';
    try {
        const res = await fetch(`${ptApi()}/admin-actions/history`);
        if (!res.ok) throw new Error('bad');
        _ptHistData = await res.json();
        ptRenderHist();
    } catch (e) {
        document.getElementById('pt-hist-list').innerHTML =
            `<div class="pt-hist-empty">No se pudo cargar el historial.</div>`;
    }
}

function ptRenderHist() {
    const list = document.getElementById('pt-hist-list');
    if (!list) return;
    let rows = _ptHistData.slice().reverse(); // más reciente primero
    if (_ptHistFilter === 'PROPERTY') rows = rows.filter(r => r.entity === 'PROPERTY');
    else if (_ptHistFilter === 'AGENT') rows = rows.filter(r => r.entity === 'AGENT');
    else if (_ptHistFilter === 'UNDO') rows = rows.filter(r => r.undoable);

    const countEl = document.getElementById('pt-hist-count');
    if (countEl) countEl.textContent = `${rows.length} acción(es) · ${_ptHistData.filter(r=>r.undoable).length} deshacible(s) en total`;

    if (!rows.length) { list.innerHTML = `<div class="pt-hist-empty">No hay acciones registradas con este filtro.</div>`; return; }

    list.innerHTML = rows.map(r => {
        const act = r.action || 'UPDATE';
        const when = r.timestamp
            ? new Date(r.timestamp).toLocaleString('es-CO', {day:'2-digit', month:'short', year:'numeric', hour:'2-digit', minute:'2-digit'})
            : '—';
        const badge = r.undoable
            ? `<span class="pt-hist-badge pt-badge-undo">Deshacible</span>`
            : `<span class="pt-hist-badge pt-badge-locked">No deshacible</span>`;
        return `<div class="pt-hist-row">
      <div class="pt-hist-dot pt-act-${act}">${PT_ACT_ICON[act]||PT_ACT_ICON.UPDATE}</div>
      <div class="pt-hist-main">
        <div class="pt-hist-desc">${ptEsc(r.description || (PT_ACT_LABEL[act]||act))}</div>
        <div class="pt-hist-meta">
          <span>${PT_ACT_LABEL[act]||act}</span><span>·</span>
          <span>${PT_ENT_LABEL[r.entity]||r.entity||'—'}</span><span>·</span>
          <span>${ptEsc(r.performedBy||'Sistema')}</span><span>·</span>
          <span>${when}</span>
          ${badge}
        </div>
      </div>
    </div>`;
    }).join('');
}

function ptEsc(s) {
    return String(s == null ? '' : s)
        .replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')
        .replace(/"/g,'&quot;').replace(/'/g,'&#39;');
}
