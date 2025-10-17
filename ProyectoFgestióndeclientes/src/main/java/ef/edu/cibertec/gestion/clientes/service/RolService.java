package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Rol;
import java.util.List;

public interface RolService {
	// Listar todos los roles
    List<Rol> listarRoles();

    // Obtener un rol por ID
    Rol obtenerRolPorId(Integer id);

    // Crear un nuevo rol
    Rol crearRol(Rol rol);

    // Actualizar un rol
    Rol actualizarRol(Integer id, Rol rol);

    // Eliminar un rol
    void eliminarRol(Integer id);

    // Buscar un rol por nombre
    Rol buscarPorNombre(String nombre);
}
