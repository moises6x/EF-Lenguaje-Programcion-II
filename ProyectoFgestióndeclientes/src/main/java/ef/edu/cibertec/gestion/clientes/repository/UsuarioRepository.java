package ef.edu.cibertec.gestion.clientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar por nombre de usuario (ignora mayúsculas/minúsculas)
    Optional<Usuario> findByUsernameIgnoreCase(String username);

    // Verificar si ya existe un username
    boolean existsByUsernameIgnoreCase(String username);
}

