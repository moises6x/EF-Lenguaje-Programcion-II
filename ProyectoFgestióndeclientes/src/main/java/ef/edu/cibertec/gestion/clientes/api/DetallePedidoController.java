package ef.edu.cibertec.gestion.clientes.api;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import ef.edu.cibertec.gestion.clientes.repository.DetallePedidoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/detalle-pedidos")
@RequiredArgsConstructor
public class DetallePedidoController {

    private final DetallePedidoRepository repository;

    // 🔹 Crear un nuevo detalle de pedido
    @PostMapping
    public ResponseEntity<DetallePedido> crear(@Valid @RequestBody DetallePedido detallePedido) {
        log.info("POST /api/detalle-pedidos");
        DetallePedido saved = repository.save(detallePedido);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Listar todos los detalles de pedido
    @GetMapping
    public List<DetallePedido> listar() {
        log.info("GET /api/detalle-pedidos");
        return repository.findAll();
    }

    // 🔹 Obtener un detalle de pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> obtener(@PathVariable Integer id) {
        log.info("GET /api/detalle-pedidos/{}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Listar todos los detalles de un pedido específico
    @GetMapping("/pedido/{idPedido}")
    public List<DetallePedido> listarPorPedido(@PathVariable Integer idPedido) {
        log.info("GET /api/detalle-pedidos/pedido/{}", idPedido);
        return repository.findByPedidoId(idPedido);
    }

    // 🔹 Actualizar un detalle de pedido
    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> actualizar(@PathVariable Integer id, @Valid @RequestBody DetallePedido cambios) {
        log.info("PUT /api/detalle-pedidos/{}", id);
        return repository.findById(id)
                .map(detalle -> {
                    detalle.setPedido(cambios.getPedido());
                    detalle.setProducto(cambios.getProducto());
                    detalle.setCantidad(cambios.getCantidad());
                
                    return ResponseEntity.ok(repository.save(detalle));
                }).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Eliminar un detalle de pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/detalle-pedidos/{}", id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

