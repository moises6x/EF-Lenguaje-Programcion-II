package ef.edu.cibertec.gestion.clientes.api;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.api.request.ProductoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.ProductoResponseDto;
import ef.edu.cibertec.gestion.clientes.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;

    // Listar
    @GetMapping
    public List<ProductoResponseDto> listarTodos() {
        log.info("GET /api/productos");
        return service.listar();
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ProductoResponseDto obtenerPorId(@PathVariable Integer id) {
        log.info("GET /api/productos/{}", id);
        return service.obtener(id);
    }

    // Crear
    @PostMapping
    public ResponseEntity<ProductoResponseDto> crear(@Valid @RequestBody ProductoRequestDto request) {
        log.info("POST /api/productos");
        ProductoResponseDto saved = service.crear(request);
        return ResponseEntity.created(URI.create("/api/productos/" + saved.getId())).body(saved);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ProductoResponseDto actualizar(@PathVariable Integer id,
                                          @Valid @RequestBody ProductoRequestDto request) {
        log.info("PUT /api/productos/{}", id);
        return service.actualizar(id, request);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/productos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Activos
    @GetMapping("/activos")
    public List<ProductoResponseDto> listarActivos() {
        log.info("GET /api/productos/activos");
        return service.listarActivos();
    }

    // Por rango de precio
    @GetMapping("/precio")
    public List<ProductoResponseDto> listarPorRangoPrecio(@RequestParam BigDecimal min,
                                                          @RequestParam BigDecimal max) {
        log.info("GET /api/productos/precio?min={}&max={}", min, max);
        return service.listarPorRangoPrecio(min, max);
    }
}
