package ef.edu.cibertec.gestion.clientes.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository repository;

    // Crear cliente
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente) {
        log.info("POST /api/clientes");

        // Establecer la fecha actual si no viene en el JSON
        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }

        Cliente saved = repository.save(cliente);
        return ResponseEntity.ok(saved);
    }

    // Listar todos los clientes
    @GetMapping
    public List<Cliente> listar() {
        log.info("GET /api/clientes");
        return repository.findAll();
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtener(@PathVariable Integer id) {
        log.info("GET /api/clientes/{}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cambios) {
        log.info("PUT /api/clientes/{}", id);
        return repository.findById(id)
                .map(c -> {
                    c.setNombre(cambios.getNombre());
                    c.setApellido(cambios.getApellido());
                    c.setCorreo(cambios.getCorreo());
                    c.setDni(cambios.getDni());
                    c.setTelefono(cambios.getTelefono());
                    c.setDirecciones(cambios.getDirecciones());
                    c.setPedidos(cambios.getPedidos());
                    return ResponseEntity.ok(repository.save(c));
                }).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/clientes/{}", id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar cliente por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Cliente> buscarPorDni(@PathVariable String dni) {
        log.info("GET /api/clientes/dni/{}", dni);
        Optional<Cliente> cliente = repository.findByDni(dni);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}


