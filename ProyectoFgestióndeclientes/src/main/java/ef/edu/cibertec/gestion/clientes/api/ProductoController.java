package ef.edu.cibertec.gestion.clientes.api;



import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Producto;
import ef.edu.cibertec.gestion.clientes.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository productoRepository;

    // 🟢 Obtener todos los productos
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // 🔵 Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🟡 Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevo = productoRepository.save(producto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // 🟠 Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return productoRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(producto.getNombre());
                    existing.setDescripcion(producto.getDescripcion());
                    existing.setPrecio(producto.getPrecio());
                    existing.setStock(producto.getStock());
                    existing.setEstado(producto.getEstado());
                    Producto actualizado = productoRepository.save(existing);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔴 Eliminar un producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 🟣 Buscar productos activos
    @GetMapping("/activos")
    public List<Producto> listarActivos() {
        return productoRepository.findByEstado("Activo");
    }
}
