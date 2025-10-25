package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.LoginRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.UsuarioRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.UsuarioResponseDto;
import ef.edu.cibertec.gestion.clientes.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    // Listar (ahora sí devuelve usuarios)
    @GetMapping
    public List<UsuarioResponseDto> listarUsuarios() {
        log.info("GET /api/usuarios");
        return service.listarUsuarios();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public UsuarioResponseDto obtenerUsuario(@PathVariable Integer id) {
        log.info("GET /api/usuarios/{}", id);
        return service.obtenerUsuario(id);
    }

    // Crear
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crearUsuario(@Valid @RequestBody UsuarioRequestDto request) {
        log.info("POST /api/usuarios");
        UsuarioResponseDto saved = service.crearUsuario(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + saved.getId())).body(saved);
    }

    // Actualizar
    @PutMapping("/{id}")
    public UsuarioResponseDto actualizarUsuario(@PathVariable Integer id,
                                                @Valid @RequestBody UsuarioRequestDto request) {
        log.info("PUT /api/usuarios/{}", id);
        return service.actualizarUsuario(id, request);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        log.info("DELETE /api/usuarios/{}", id);
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por username
    @GetMapping("/buscar")
    public UsuarioResponseDto buscarPorNombre(@RequestParam String username) {
        log.info("GET /api/usuarios/buscar?username={}", username);
        return service.buscarPorNombre(username);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        log.info("POST /api/usuarios/login");
        String result = service.login(loginRequest);
        return ResponseEntity.ok(result);
    }
}

