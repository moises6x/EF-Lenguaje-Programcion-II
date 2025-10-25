package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.RolRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.RolResponseDto;

public interface RolService {

    List<RolResponseDto> listarRoles();

    RolResponseDto obtenerRolPorId(Integer id);

    RolResponseDto crearRol(RolRequestDto request);

    RolResponseDto actualizarRol(Integer id, RolRequestDto request);

    void eliminarRol(Integer id);

    RolResponseDto buscarPorNombre(String nombre);
}

