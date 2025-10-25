package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.PedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.PedidoResponseDto;

public interface PedidoService {

    PedidoResponseDto crear(PedidoRequestDto request);

    List<PedidoResponseDto> listar();

    PedidoResponseDto obtener(Integer id);

    List<PedidoResponseDto> listarPorClienteId(Integer idCliente);

    List<PedidoResponseDto> listarPorEstado(String estado);

    PedidoResponseDto actualizar(Integer id, PedidoRequestDto request);

    void eliminar(Integer id);
}
