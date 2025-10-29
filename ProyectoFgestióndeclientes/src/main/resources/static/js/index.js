// ======================
// index.js (COMPLETO)
// ======================

// ---------- Año footer ----------
const yearEl = document.getElementById('year');
if (yearEl) yearEl.textContent = new Date().getFullYear();

// ---------- Slider ----------
const slides = Array.from(document.querySelectorAll('.slide'));
const dotsBox = document.getElementById('dots');
let idx = 0, timer;

function paintDots(){
  if(!dotsBox) return;
  dotsBox.innerHTML = slides
    .map((_,i)=>`<span class="dot ${i===idx?'active':''}" data-i="${i}"></span>`)
    .join('');
  dotsBox.querySelectorAll('.dot').forEach(d=>d.onclick = ()=>go(+d.dataset.i));
}
function show(){
  slides.forEach((s,i)=>s.classList.toggle('active', i===idx));
  paintDots();
}
function next(){ idx = (idx+1)%slides.length; show(); }
function go(i){ idx=i; show(); restart(); }
function start(){ if(slides.length){ timer = setInterval(next, 4800); } }
function restart(){ clearInterval(timer); start(); }
if (slides.length) { show(); start(); }

// ==============================
// Carga de productos desde la API
// ==============================

// Lista global de productos que vienen de la BD.
// Cada item: { id, nombre, descripcion, precio, stock, estado }
let PRODUCTS_DB = [];

// Render de cards en #gridProducts
const grid = document.getElementById('gridProducts');

// Función para formatear moneda (PEN)
const PEN = new Intl.NumberFormat('es-PE', {
  style:'currency', currency:'PEN', minimumFractionDigits:2
});

// Imagen por defecto (si no tienes campo imagen en la BD)
const FALLBACK_IMG = '/img/no-image.png';

// Renderiza las tarjetas con datos de BD
function renderProducts(products){
  if(!grid) return;

  if(!products?.length){
    grid.innerHTML = `
      <div class="card" style="grid-column:1/-1;padding:1.5rem;text-align:center">
        No hay productos para mostrar.
      </div>`;
    return;
  }

  grid.innerHTML = products.map(p=>{
    const precio = Number(p.precio || 0);
    const agotado = !p.stock || p.stock <= 0;
    const img = p.imagenUrl || FALLBACK_IMG; // si luego agregas imagenUrl en tu API

    return `
      <article class="card">
        <div class="thumb"><img src="${img}" alt="${p.nombre}" onerror="this.src='${FALLBACK_IMG}'"></div>
        <div class="info">
          <div class="brand">${p.estado || ''}</div>
          <div class="title">${p.nombre}</div>
          <div class="price">
            <div class="now">${PEN.format(precio)}</div>
          </div>
          <div class="muted" style="color:#6b7280;font-size:.9rem;margin-top:.25rem">
            ${p.descripcion ? escapeHtml(p.descripcion) : ''}
          </div>
          <div class="muted" style="color:#6b7280;font-size:.9rem;margin-top:.25rem">
            Stock: <strong>${p.stock ?? 0}</strong>
          </div>
        </div>
        <div class="buy">
          <button class="btn ghost" data-id="${p.id}" aria-label="Ver">👁️ Ver</button>
          <button class="btn primary add" data-id="${p.id}" ${agotado?'disabled':''}>
            ${agotado ? 'Agotado' : 'Añadir'}
          </button>
        </div>
      </article>
    `;
  }).join('');

  // activar botones de añadir
  grid.querySelectorAll('.add').forEach(btn=>{
    btn.addEventListener('click', ()=> addToCart(+btn.dataset.id));
  });
}

// Helper para evitar XSS al imprimir descripción
function escapeHtml(str){
  return String(str)
    .replaceAll('&','&amp;')
    .replaceAll('<','&lt;')
    .replaceAll('>','&gt;')
    .replaceAll('"','&quot;')
    .replaceAll("'",'&#39;');
}

// Carga desde la API
async function loadProducts(){
  try {
    // Usa activos o todos: '/api/productos/activos' o '/api/productos'
    const res = await fetch('/api/productos/activos');
    if(!res.ok) throw new Error('HTTP '+res.status);
    const data = await res.json();

    // Normaliza campos por si vienen null
    PRODUCTS_DB = (data || []).map(p => ({
      id: p.id,
      nombre: p.nombre ?? '',
      descripcion: p.descripcion ?? '',
      precio: Number(p.precio ?? 0),
      stock: Number.isFinite(p.stock) ? p.stock : 0,
      estado: p.estado ?? 'Activo',
      // imagenUrl: p.imagenUrl ?? null, // si luego lo agregas en tu DTO
    }));

    renderProducts(PRODUCTS_DB);
  } catch (e) {
    console.error('Error cargando productos:', e);
    if(grid){
      grid.innerHTML = `
        <div class="card" style="grid-column:1/-1;padding:1.5rem;text-align:center;color:#b91c1c">
          Ocurrió un error cargando los productos.
        </div>`;
    }
  }
}

// Arrancamos la carga
loadProducts();

// ---------- Newsletter ----------
// ---------- Newsletter (form + envío a backend) ----------
(function(){
  const form = document.getElementById('nlForm');
  if(!form) return;

  const btn   = document.getElementById('nlBtn');
  const msgEl = document.getElementById('nlMsg');

  const el = id => document.getElementById(id);
  const err = (id, text='') => {
    const small = document.querySelector(`.nl-err[data-for="${id}"]`);
    if(small) small.textContent = text;
  };

  function validate(){
    let ok = true;
    const nombre  = (el('nlNombre').value || '').trim();
    const email   = (el('nlEmail').value || '').trim();
    const tyc     = el('nlTyc').checked;

    err('nlNombre',''); err('nlEmail',''); err('nlTyc','');

    if(!nombre){ err('nlNombre','Ingresa tu nombre'); ok=false; }
    if(!/^\S+@\S+\.\S+$/.test(email)){ err('nlEmail','Correo inválido'); ok=false; }
    if(!tyc){ err('nlTyc','Debes aceptar los términos'); ok=false; }
    return ok;
  }

  async function subscribe(payload){
    const res = await fetch('/api/newsletter/subscribe', {
      method:'POST',
      headers:{ 'Content-Type':'application/json' },
      body: JSON.stringify(payload)
    });
    if(!res.ok){
      const t = await res.text().catch(()=> '');
      throw new Error(`HTTP ${res.status} - ${t}`);
    }
    return res.json().catch(()=> ({}));
  }

  form.addEventListener('submit', async (e)=>{
    e.preventDefault();
    msgEl.textContent = '';

    if(!validate()) return;

    const intereses = Array.from(form.querySelectorAll('input[name="intereses"]:checked'))
                           .map(i=> i.value);

    const payload = {
      nombre   : (el('nlNombre').value || '').trim(),
      email    : (el('nlEmail').value   || '').trim(),
      telefono : (el('nlTelefono').value|| '').trim(),
      intereses: intereses
    };

    btn.disabled = true;
    btn.textContent = 'Enviando…';

    try {
      const data = await subscribe(payload);
      msgEl.textContent = data?.message || '¡Listo! Revisa tu correo para confirmar.';
      form.reset();
    } catch (errObj){
      console.error(errObj);
      msgEl.textContent = 'Ocurrió un error al suscribirte. Intenta nuevamente.';
    } finally {
      btn.disabled = false;
      btn.textContent = 'Suscribirme';
    }
  });
})();


// ---------- Búsqueda ----------
const btnSearch = document.getElementById('btnSearch');
if (btnSearch) {
  btnSearch.onclick = () => {
    const term = (document.getElementById('q')?.value || '').toLowerCase();

    // filtra sobre PRODUCTS_DB y re-renderiza
    const filtered = PRODUCTS_DB.filter(p => {
      const t = `${p.nombre} ${p.estado}`.toLowerCase();
      return t.includes(term);
    });
    renderProducts(filtered);
  };
}

// ==============================
// Carrito (localStorage + drawer) usando productos de BD
// ==============================

const findProduct = id => PRODUCTS_DB.find(p => p.id === id);

// Estado
const cartCount = document.getElementById('cartCount');
const CART_KEY = 'cart-demo';
const cart = JSON.parse(localStorage.getItem(CART_KEY) || '[]');

function saveCart(){ localStorage.setItem(CART_KEY, JSON.stringify(cart)); }
function updateCartBadge(){
  if(!cartCount) return;
  cartCount.textContent = cart.reduce((a,b)=>a + (b.qty||0), 0);
}
updateCartBadge();

// Añadir al carrito (usa productos de BD)
function addToCart(id){
  const prod = findProduct(id);
  if(!prod){
    alert('Producto no disponible');
    return;
  }
  if(prod.stock <= 0){
    alert('Producto agotado');
    return;
  }

  const item = cart.find(x=>x.id===id);
  if(item) {
    // opcional: validar no superar stock
    if(item.qty + 1 > prod.stock){
      alert('Stock insuficiente');
      return;
    }
    item.qty++;
  } else {
    cart.push({id, qty:1});
  }
  saveCart(); updateCartBadge(); renderCart();
  if(cartCount){
    cartCount.animate(
      [{transform:'scale(1)'},{transform:'scale(1.2)'},{transform:'scale(1)'}],
      {duration:300}
    );
  }
}

// Offcanvas
const cartDrawerEl = document.getElementById('cartDrawer');
let cartDrawer = null;
if (cartDrawerEl && window.bootstrap?.Offcanvas) {
  cartDrawer = new bootstrap.Offcanvas(cartDrawerEl);
}

// Abrir drawer desde el botón del header
const btnCart = document.getElementById('btnCart');
if (btnCart) {
  btnCart.addEventListener('click', ()=>{
    renderCart();
    if (cartDrawer) cartDrawer.show();
  });
}

// Render del carrito
function renderCart(){
  const list = document.getElementById('cartList');
  const subEl = document.getElementById('cartSubtotal');
  if(!list || !subEl) return;

  if(cart.length === 0){
    list.innerHTML = `
      <div class="text-center text-muted" style="padding:2rem 0">
        <div style="font-size:2.2rem">🛍️</div>
        <p class="mt-2">Tu carrito está vacío</p>
      </div>`;
    subEl.textContent = PEN.format(0);
    return;
  }

  let subtotal = 0;
  list.innerHTML = cart.map(line => {
    const p = findProduct(line.id);
    if(!p) return ''; // si el producto ya no existe, omitimos

    const lineTotal = (Number(p.precio)||0) * (line.qty||0);
    subtotal += lineTotal;

    return `
      <div class="cart-item" data-id="${p.id}">
        <div class="cart-thumb">
          <img src="${p.imagenUrl || FALLBACK_IMG}" alt="${p.nombre}" onerror="this.src='${FALLBACK_IMG}'">
        </div>
        <div>
          <div class="cart-title">${p.nombre}</div>
          <div class="cart-meta">
            ${PEN.format(Number(p.precio)||0)} × ${line.qty} · Stock: ${p.stock}
          </div>
          <div class="cart-actions mt-2">
            <button class="cart-remove" title="Quitar">Quitar</button>
          </div>
        </div>
        <div class="text-end">
          <div><strong>${PEN.format(lineTotal)}</strong></div>
        </div>
      </div>
    `;
  }).join('');

  subEl.textContent = PEN.format(subtotal);
}

// Delegación de eventos dentro del drawer (quitar ítems)
const cartListEl = document.getElementById('cartList');
if (cartListEl) {
  cartListEl.addEventListener('click', (e)=>{
    const itemEl = e.target.closest('.cart-item');
    if(!itemEl) return;
    const id = +itemEl.dataset.id;

    if(e.target.classList.contains('cart-remove')){
      const idx = cart.findIndex(x=>x.id===id);
      if(idx !== -1){
        cart.splice(idx,1);
        saveCart(); updateCartBadge(); renderCart();
      }
    }
  });
}

// Vaciar carrito
const btnClearCart = document.getElementById('btnClearCart');
if (btnClearCart) {
  btnClearCart.addEventListener('click', ()=>{
    if(!cart.length) return;
    if(confirm('¿Vaciar todo el carrito?')){
      cart.splice(0, cart.length);
      saveCart(); updateCartBadge(); renderCart();
    }
  });
}

// Continuar compra
const btnCheckout = document.getElementById('btnCheckout');
if (btnCheckout) {
  btnCheckout.addEventListener('click', ()=>{
    // Crea la ruta /checkout en tu UiController si aún no existe
    window.location.href = '/checkout';
  });
}

