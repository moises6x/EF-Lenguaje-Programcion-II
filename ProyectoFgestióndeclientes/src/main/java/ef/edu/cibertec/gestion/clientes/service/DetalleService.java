package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.DetallePedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DetallePedidoResponseDto;

public interface DetalleService {

    // Crear un nuevo detalle de pedido
    DetallePedidoResponseDto crear(DetallePedidoRequestDto request);

    // Listar todos los detalles de pedido
    List<DetallePedidoResponseDto> listar();

    // Obtener un detalle de pedido por ID
    DetallePedidoResponseDto obtener(Integer id);

    // Listar detalles de un pedido específico
    List<DetallePedidoResponseDto> listarPorPedido(Integer idPedido);

    // Actualizar un detalle de pedido
    DetallePedidoResponseDto actualizar(Integer id, DetallePedidoRequestDto request);

    // Eliminar un detalle de pedido
    void eliminar(Integer id);
}

