package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.DireccionRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DireccionResponseDto;

public interface DireccionService {

    DireccionResponseDto crear(DireccionRequestDto request);

    List<DireccionResponseDto> listar();

    DireccionResponseDto obtener(Integer id);

    List<DireccionResponseDto> listarPorClienteId(Integer idCliente);

    DireccionResponseDto actualizar(Integer id, DireccionRequestDto request);

    void eliminar(Integer id);
}

