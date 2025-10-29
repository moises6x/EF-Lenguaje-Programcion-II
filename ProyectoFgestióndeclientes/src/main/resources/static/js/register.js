(function() {
  const form = document.getElementById('registerForm');
  const alertArea = document.getElementById('alertArea');
  const btn = document.getElementById('btnRegister');
  const spinner = btn.querySelector('.spinner-border');
  const btnText = btn.querySelector('.btn-text');

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
    btnText.textContent = isLoading ? 'Creando cuenta...' : 'Registrarme';
  }

  function validate() {
    let ok = true;
    const u = form.username, p = form.password, c = form.confirmPassword;
    if (!u.value.trim()) { u.classList.add('is-invalid'); ok = false; } else u.classList.remove('is-invalid');
    if (!p.value.trim() || p.value.length < 6) { p.classList.add('is-invalid'); ok = false; } else p.classList.remove('is-invalid');
    if (p.value !== c.value) { c.classList.add('is-invalid'); ok = false; } else c.classList.remove('is-invalid');
    return ok;
  }

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
      const resp = await fetch('/api/usuarios', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (resp.ok) {
        showAlert('✅ Cuenta creada con éxito. Redirigiendo al login...', 'success');
        setTimeout(() => window.location.href = '/login', 1500);
      } else {
        const text = await resp.text();
        showAlert(text || '❌ Error al crear la cuenta.', 'danger');
      }
    } catch (err) {
      console.error(err);
      showAlert('❌ Error de conexión con el servidor.', 'danger');
    } finally {
      setLoading(false);
    }
  });
})();
