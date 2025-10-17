package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;
import java.util.List;

public interface UsuarioService {
	// Listar todos los usuarios
    List<Usuario> listarUsuarios();

    // Obtener un usuario por ID
    Usuario obtenerUsuario(Integer id);

    // Crear un nuevo usuario
    Usuario crearUsuario(Usuario usuario);

    // Actualizar un usuario
    Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado);

    // Eliminar un usuario
    void eliminarUsuario(Integer id);

    // Buscar un usuario por nombre
    Usuario buscarPorNombre(String username);

    // Validar login
    String login(Usuario loginRequest);
}
