package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ef.edu.cibertec.gestion.clientes.api.request.ClienteRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteUpdateDto;
import ef.edu.cibertec.gestion.clientes.api.response.ClienteResponseDto;
import ef.edu.cibertec.gestion.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDto> crear(@Valid @RequestBody ClienteRequestDto request) {
        log.info("POST /api/clientes");
        ClienteResponseDto saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/clientes/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<ClienteResponseDto> listar() {
        log.info("GET /api/clientes");
        return service.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/clientes/{}", id);
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public ClienteResponseDto actualizar(@PathVariable Integer id,
                                         @Valid @RequestBody ClienteUpdateDto request) { // <-- Usa el nuevo DTO
        log.info("PUT /api/clientes/{}", id);
        return service.actualizar(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/clientes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ClienteResponseDto buscarPorDni(@PathVariable String dni) {
        log.info("GET /api/clientes/dni/{}", dni);
        return service.buscarPorDni(dni);
    }
}



