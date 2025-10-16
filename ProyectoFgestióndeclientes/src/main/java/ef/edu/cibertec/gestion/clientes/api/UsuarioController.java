package ef.edu.cibertec.gestion.clientes.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Usuario;
import ef.edu.cibertec.gestion.clientes.repository.UsuarioRepository;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    // 🟢 Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 🔵 Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🟡 Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByUsernameIgnoreCase(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El nombre de usuario ya está en uso.");
        }

        usuario.setFechaCreacion(LocalDateTime.now());
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // 🟠 Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setUsername(usuarioActualizado.getUsername());
                    u.setPassword(usuarioActualizado.getPassword());
                    u.setRoles(usuarioActualizado.getRoles());
                    Usuario actualizado = usuarioRepository.save(u);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔴 Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 🟣 Buscar usuario por nombre
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorNombre(@RequestParam String username) {
        Optional<Usuario> usuario = usuarioRepository.findByUsernameIgnoreCase(username);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
 // 🟢 Login simple (validación por username y password)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario loginRequest) {
        log.info("POST /api/usuarios/login");

        return usuarioRepository.findByUsernameIgnoreCase(loginRequest.getUsername())
                .filter(u -> u.getPassword().equals(loginRequest.getPassword()))
                .map(u -> ResponseEntity.ok("✅ Login exitoso. Bienvenido " + u.getUsername() + "!"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("❌ Credenciales inválidas. Verifique usuario o contraseña."));
    }
    
}

