package ef.edu.cibertec.gestion.clientes.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;
import ef.edu.cibertec.gestion.clientes.repository.UsuarioRepository;
import ef.edu.cibertec.gestion.clientes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class UsuarioServiceImpl implements UsuarioService {
	private final UsuarioRepository repository;

    @Override
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    @Override
    public Usuario obtenerUsuario(Integer id) {
        Optional<Usuario> usuario = repository.findById(id);
        return usuario.orElse(null);
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (repository.existsByUsernameIgnoreCase(usuario.getUsername())) {
            return null; // Retorna null si el username ya existe
        }
        usuario.setFechaCreacion(LocalDateTime.now());
        return repository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = repository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setUsername(usuarioActualizado.getUsername());
            usuario.setPassword(usuarioActualizado.getPassword());
            usuario.setRoles(usuarioActualizado.getRoles());
            return repository.save(usuario);
        }
        return null;
    }

    @Override
    public void eliminarUsuario(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Usuario buscarPorNombre(String username) {
        Optional<Usuario> usuario = repository.findByUsernameIgnoreCase(username);
        return usuario.orElse(null);
    }

    @Override
    public String login(Usuario loginRequest) {
        log.info("POST /api/usuarios/login");
        return repository.findByUsernameIgnoreCase(loginRequest.getUsername())
                .filter(u -> u.getPassword().equals(loginRequest.getPassword()))
                .map(u -> "✅ Login exitoso. Bienvenido " + u.getUsername() + "!")
                .orElse("❌ Credenciales inválidas. Verifique usuario o contraseña.");
    }
}
