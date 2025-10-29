(function() {
  const form        = document.getElementById('loginForm');
  const btn         = document.getElementById('btnLogin');
  const btnRegister = document.getElementById('btnRegister');
  const togglePwd   = document.getElementById('togglePwd');
  const alertArea   = document.getElementById('alertArea');
  const split       = document.getElementById('split');
  const spinner     = btn.querySelector('.spinner-border');
  const btnText     = btn.querySelector('.btn-text');

  /* ==== Helpers ==== */
  function showAlert(message, type = 'success') {
    alertArea.innerHTML = `
      <div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
      </div>`;
  }

  function setLoading(isLoading) {
    btn.disabled = isLoading;
    spinner.classList.toggle('d-none', !isLoading);
    btnText.textContent = isLoading ? 'Ingresando...' : 'Iniciar sesión';
  }

  function validate() {
    let ok = true;
    const u = form.username, p = form.password;
    if (!u.value.trim()) { u.classList.add('is-invalid'); ok = false; } else u.classList.remove('is-invalid');
    if (!p.value)        { p.classList.add('is-invalid'); ok = false; } else p.classList.remove('is-invalid');
    return ok;
  }

  /* ==== Mostrar/ocultar contraseña ==== */
  togglePwd.addEventListener('click', () => {
    const input = document.getElementById('password');
    const isPwd = input.getAttribute('type') === 'password';
    input.setAttribute('type', isPwd ? 'text' : 'password');
    togglePwd.classList.toggle('btn-outline-secondary');
    togglePwd.classList.toggle('btn-outline-primary');
  });

  /* ==== Ir a registro ==== */
  btnRegister.addEventListener('click', () => {
    window.location.href = '/registro';   // crea esta vista/controlador si no existe
  });

  /* ==== Submit (login) ==== */
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    alertArea.innerHTML = '';
    if (!validate()) return;

    setLoading(true);

    const payload = {
      username: form.username.value.trim(),
      password: form.password.value
    };

    try {
      const resp = await fetch('/api/usuarios/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const text = await resp.text();

      if (resp.ok && /Login exitoso/i.test(text)) {
        showAlert('✅ Ingreso correcto. Redirigiendo...', 'success');

        // SWAP: formulario ⇄ imagen
        split.classList.add('swap');

        // redirigir cuando termine la animación (~900ms)
        setTimeout(() => {
          window.location.href = '/';  // ajusta a la ruta que tengas
        }, 1000);

      } else {
        showAlert(text || '❌ Credenciales inválidas.', 'danger');
      }
    } catch (err) {
      console.error(err);
      showAlert('❌ Error de red o del servidor.', 'danger');
    } finally {
      setLoading(false);
    }
  });
})();
