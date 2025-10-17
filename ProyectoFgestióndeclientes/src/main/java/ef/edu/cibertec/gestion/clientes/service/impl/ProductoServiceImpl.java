package ef.edu.cibertec.gestion.clientes.service.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.Producto;
import ef.edu.cibertec.gestion.clientes.repository.ProductoRepository;
import ef.edu.cibertec.gestion.clientes.service.ProductoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductoServiceImpl implements ProductoService {
	
	
	private final ProductoRepository repository;

    @Override
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Producto obtenerPorId(Integer id) {
        Optional<Producto> producto = repository.findById(id);
        return producto.orElse(null);
    }

    @Override
    public Producto crear(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Producto actualizar(Integer id, Producto producto) {
        Optional<Producto> productoExistente = repository.findById(id);
        if (productoExistente.isPresent()) {
            Producto existing = productoExistente.get();
            existing.setNombre(producto.getNombre());
            existing.setDescripcion(producto.getDescripcion());
            existing.setPrecio(producto.getPrecio());
            existing.setStock(producto.getStock());
            existing.setEstado(producto.getEstado());
            return repository.save(existing);
        }
        return null;
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Producto> listarActivos() {
        return repository.findByEstado("Activo");
    }

    @Override
    public List<Producto> findByRangoPrecio(double min, double max) {
        return repository.findByRangoPrecio(min, max);
    }

}
