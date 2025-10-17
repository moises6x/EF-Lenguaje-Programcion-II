package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;
import java.util.List;
public interface DireccionService {
	
	
	// Crear una nueva dirección
    Direccion crear(Direccion direccion);

    // Listar todas las direcciones
    List<Direccion> listar();

    // Obtener una dirección por ID
    Direccion obtenerPorId(Integer id);

    // Listar direcciones por ID de cliente
    List<Direccion> listarPorClienteId(Integer idCliente);

    // Actualizar una dirección
    Direccion actualizar(Integer id, Direccion cambios);

    // Eliminar una dirección
    void eliminar(Integer id);

}
