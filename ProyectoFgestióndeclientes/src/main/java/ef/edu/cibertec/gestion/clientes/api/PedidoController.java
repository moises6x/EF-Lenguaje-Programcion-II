package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.PedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.PedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> crear(@Valid @RequestBody PedidoRequestDto request) {
        log.info("POST /api/pedidos");
        PedidoResponseDto saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<PedidoResponseDto> listar() {
        log.info("GET /api/pedidos");
        return service.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/pedidos/{}", id);
        return service.obtener(id);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<PedidoResponseDto> listarPorCliente(@PathVariable Integer idCliente) {
        log.info("GET /api/pedidos/cliente/{}", idCliente);
        return service.listarPorClienteId(idCliente);
    }

    @GetMapping("/estado/{estado}")
    public List<PedidoResponseDto> listarPorEstado(@PathVariable String estado) {
        log.info("GET /api/pedidos/estado/{}", estado);
        return service.listarPorEstado(estado);
    }

    @PutMapping("/{id}")
    public PedidoResponseDto actualizar(@PathVariable Integer id,
                                        @Valid @RequestBody PedidoRequestDto request) {
        log.info("PUT /api/pedidos/{}", id);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/pedidos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
