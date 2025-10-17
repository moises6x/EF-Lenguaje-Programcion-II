package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Pedido;
import java.util.List;
public interface PedidoService {
	// Crear un nuevo pedido
    Pedido crear(Pedido pedido);

    // Listar todos los pedidos
    List<Pedido> listar();

    // Obtener un pedido por ID
    Pedido obtenerPorId(Integer id);

    // Listar pedidos por ID de cliente
    List<Pedido> listarPorClienteId(Integer idCliente);

    // Listar pedidos por estado
    List<Pedido> listarPorEstado(String estado);

    // Actualizar un pedido
    Pedido actualizar(Integer id, Pedido cambios);

    // Eliminar un pedido
    void eliminar(Integer id);
}
