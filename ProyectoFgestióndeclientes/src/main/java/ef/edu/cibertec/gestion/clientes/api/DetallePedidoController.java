package ef.edu.cibertec.gestion.clientes.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.DetallePedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DetallePedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.service.DetalleService; // <- tu interfaz con DTOs
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/detalle-pedidos")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetalleService service;

    // Crear
    @PostMapping
    public ResponseEntity<DetallePedidoResponseDto> crear(@Valid @RequestBody DetallePedidoRequestDto request) {
        log.info("POST /api/detalle-pedidos");
        DetallePedidoResponseDto saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/detalle-pedidos/" + saved.getId())).body(saved);
    }

    // Listar todos
    @GetMapping
    public List<DetallePedidoResponseDto> listar() {
        log.info("GET /api/detalle-pedidos");
        return service.listar();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public DetallePedidoResponseDto obtener(@PathVariable Integer id) {
        log.info("GET /api/detalle-pedidos/{}", id);
        return service.obtener(id);
    }

    // Listar por idPedido
    @GetMapping("/pedido/{idPedido}")
    public List<DetallePedidoResponseDto> listarPorPedido(@PathVariable Integer idPedido) {
        log.info("GET /api/detalle-pedidos/pedido/{}", idPedido);
        return service.listarPorPedido(idPedido);
    }

    // Actualizar
    @PutMapping("/{id}")
    public DetallePedidoResponseDto actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody DetallePedidoRequestDto request) {
        log.info("PUT /api/detalle-pedidos/{}", id);
        return service.actualizar(id, request);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/detalle-pedidos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

