package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.LoginRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.UsuarioRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.UsuarioResponseDto;

public interface UsuarioService {

    List<UsuarioResponseDto> listarUsuarios();

    UsuarioResponseDto obtenerUsuario(Integer id);

    UsuarioResponseDto crearUsuario(UsuarioRequestDto request);

    UsuarioResponseDto actualizarUsuario(Integer id, UsuarioRequestDto request);

    void eliminarUsuario(Integer id);

    UsuarioResponseDto buscarPorNombre(String username);

    String login(LoginRequestDto loginRequest);
}
