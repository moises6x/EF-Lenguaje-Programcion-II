package ef.edu.cibertec.gestion.clientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    // Permite buscar un rol por nombre (sin importar mayúsculas/minúsculas)
    Optional<Rol> findByNombreRolIgnoreCase(String nombreRol);
}


