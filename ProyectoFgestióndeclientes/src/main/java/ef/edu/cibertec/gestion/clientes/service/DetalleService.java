package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import java.util.List;

public interface DetalleService {
	
	
	
	// Crear un nuevo detalle de pedido
    DetallePedido crear(DetallePedido detallePedido);

    // Listar todos los detalles de pedido
    List<DetallePedido> listar();

    // Obtener un detalle de pedido por ID
    DetallePedido obtenerPorId(Integer id);

    // Listar detalles de un pedido específico
    List<DetallePedido> listarPorPedidoId(Integer idPedido);

    // Actualizar un detalle de pedido
    DetallePedido actualizar(Integer id, DetallePedido cambios);

    // Eliminar un detalle de pedido
    void eliminar(Integer id);
	

}
