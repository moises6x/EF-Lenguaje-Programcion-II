package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Producto;
import java.util.List;

public interface ProductoService {
	
	// Listar todos los productos
    List<Producto> listarTodos();

    // Obtener un producto por ID
    Producto obtenerPorId(Integer id);

    // Crear un nuevo producto
    Producto crear(Producto producto);

    // Actualizar un producto
    Producto actualizar(Integer id, Producto producto);

    // Eliminar un producto
    void eliminar(Integer id);

    // Listar productos activos
    List<Producto> listarActivos();

    // Buscar productos por rango de precio
    List<Producto> findByRangoPrecio(double min, double max);

}
