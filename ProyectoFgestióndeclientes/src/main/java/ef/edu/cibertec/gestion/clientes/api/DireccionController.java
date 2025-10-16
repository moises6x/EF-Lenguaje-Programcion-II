package ef.edu.cibertec.gestion.clientes.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Direccion;
import ef.edu.cibertec.gestion.clientes.repository.DireccionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionRepository repository;

    @PostMapping
    public ResponseEntity<Direccion> crear(@Valid @RequestBody Direccion direccion) {
        log.info("POST /api/direcciones");
        return ResponseEntity.ok(repository.save(direccion));
    }

    @GetMapping
    public List<Direccion> listar() {
        log.info("GET /api/direcciones");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direccion> obtener(@PathVariable Integer id) {
        log.info("GET /api/direcciones/{}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Direccion> listarPorCliente(@PathVariable Integer idCliente) {
        log.info("GET /api/direcciones/cliente/{}", idCliente);
        return repository.findByClienteId(idCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizar(@PathVariable Integer id, @Valid @RequestBody Direccion cambios) {
        log.info("PUT /api/direcciones/{}", id);
        return repository.findById(id)
                .map(d -> {
                    d.setDireccion(cambios.getDireccion());
                    d.setCiudad(cambios.getCiudad());
                    d.setProvincia(cambios.getProvincia());
                    d.setCodigoPostal(cambios.getCodigoPostal());
                    d.setPais(cambios.getPais());
                    return ResponseEntity.ok(repository.save(d));
                }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/direcciones/{}", id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

