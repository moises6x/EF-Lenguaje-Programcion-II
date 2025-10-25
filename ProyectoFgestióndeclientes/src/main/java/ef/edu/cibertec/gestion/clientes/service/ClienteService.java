package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteUpdateDto;
import ef.edu.cibertec.gestion.clientes.api.response.ClienteResponseDto;

/*
---------------------------------------------------------------------------------------------
6) ClienteService (SERVICE) → Reglas de negocio y orquestación.
   - Define QUÉ hace el dominio (crear, listar, buscar por DNI…).
   - Implementación invoca Repository y usa el Mapper.
   - Desde aquí decides validaciones extra, normalización, etc.
   Lo usa: Controller.
---------------------------------------------------------------------------------------------
*/
public interface ClienteService {
    ClienteResponseDto crear(ClienteRequestDto request);
    List<ClienteResponseDto> listar();
    ClienteResponseDto obtener(Integer id);
 // AQUÍ ESTÁ EL CAMBIO:
    ClienteResponseDto actualizar(Integer id, ClienteUpdateDto request); // <-- USA EL NUEVO DTO
    void eliminar(Integer id);
    ClienteResponseDto buscarPorDni(String dni);
}
