package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.DireccionRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DireccionResponseDto;
import ef.edu.cibertec.gestion.clientes.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService service;

    @PostMapping
    public ResponseEntity<DireccionResponseDto> crear(@Valid @RequestBody DireccionRequestDto request) {
        log.info("POST /api/direcciones");
        DireccionResponseDto saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/direcciones/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<DireccionResponseDto> listar() {
        log.info("GET /api/direcciones");
        return service.listar();
    }

    @GetMapping("/{id}")
    public DireccionResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/direcciones/{}", id);
        return service.obtener(id);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<DireccionResponseDto> listarPorCliente(@PathVariable Integer idCliente) {
        log.info("GET /api/direcciones/cliente/{}", idCliente);
        return service.listarPorClienteId(idCliente);
    }

    @PutMapping("/{id}")
    public DireccionResponseDto actualizar(@PathVariable Integer id,
                                           @Valid @RequestBody DireccionRequestDto request) {
        log.info("PUT /api/direcciones/{}", id);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/direcciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}


