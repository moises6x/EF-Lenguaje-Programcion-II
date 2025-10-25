package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.RolRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.RolResponseDto;
import ef.edu.cibertec.gestion.clientes.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService service;

    // Listar
    @GetMapping
    public List<RolResponseDto> listarRoles() {
        log.info("GET /api/roles");
        return service.listarRoles();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public RolResponseDto obtenerRolPorId(@PathVariable Integer id) {
        log.info("GET /api/roles/{}", id);
        return service.obtenerRolPorId(id);
    }

    // Crear
    @PostMapping
    public ResponseEntity<RolResponseDto> crearRol(@Valid @RequestBody RolRequestDto request) {
        log.info("POST /api/roles");
        RolResponseDto saved = service.crearRol(request);
        return ResponseEntity.created(URI.create("/api/roles/" + saved.getId())).body(saved);
    }

    // Actualizar
    @PutMapping("/{id}")
    public RolResponseDto actualizarRol(@PathVariable Integer id,
                                        @Valid @RequestBody RolRequestDto request) {
        log.info("PUT /api/roles/{}", id);
        return service.actualizarRol(id, request);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        log.info("DELETE /api/roles/{}", id);
        service.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    public RolResponseDto buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/roles/buscar?nombre={}", nombre);
        return service.buscarPorNombre(nombre);
    }
}

