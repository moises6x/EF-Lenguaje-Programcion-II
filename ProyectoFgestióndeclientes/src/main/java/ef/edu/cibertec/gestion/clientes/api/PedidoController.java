package ef.edu.cibertec.gestion.clientes.api;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Pedido;
import ef.edu.cibertec.gestion.clientes.repository.PedidoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoRepository repository;

    // 🟢 Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {
        log.info("POST /api/pedidos");

        // Si no se envía una fecha, se asigna la actual automáticamente
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }

        Pedido saved = repository.save(pedido);
        return ResponseEntity.ok(saved);
    }

    // 🟢 Listar todos los pedidos
    @GetMapping
    public List<Pedido> listar() {
        log.info("GET /api/pedidos");
        return repository.findAll();
    }

    // 🟢 Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable Integer id) {
        log.info("GET /api/pedidos/{}", id);
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🟢 Listar pedidos por cliente
    @GetMapping("/cliente/{idCliente}")
    public List<Pedido> listarPorCliente(@PathVariable Integer idCliente) {
        log.info("GET /api/pedidos/cliente/{}", idCliente);
        return repository.findByClienteId(idCliente);
    }

    // 🟢 Listar pedidos por estado (Ej: Pendiente, Completado, Cancelado)
    @GetMapping("/estado/{estado}")
    public List<Pedido> listarPorEstado(@PathVariable String estado) {
        log.info("GET /api/pedidos/estado/{}", estado);
        return repository.findByEstado(estado);
    }

    // 🔴  Actualizar un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Integer id, @Valid @RequestBody Pedido cambios) {
        log.info("PUT /api/pedidos/{}", id);
        return repository.findById(id)
                .map(pedido -> {
                    pedido.setCliente(cambios.getCliente());
                    pedido.setEstado(cambios.getEstado());
                    pedido.setTotal(cambios.getTotal());
                    pedido.setDetalles(cambios.getDetalles());
                    return ResponseEntity.ok(repository.save(pedido));
                }).orElse(ResponseEntity.notFound().build());
    }

    // 🔴  Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/pedidos/{}", id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
