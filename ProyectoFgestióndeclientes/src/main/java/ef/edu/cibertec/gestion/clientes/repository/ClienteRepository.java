package ef.edu.cibertec.gestion.clientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.Cliente;

/*
---------------------------------------------------------------------------------------------
5) ClienteRepository (REPOSITORY) → capa de ACCESO A DATOS.
   - Extiende JpaRepository: CRUD listo contra la BD (Hibernate/JPA).
   - Aquí pones queries específicas (findByDni, existsByCorreoIgnoreCase, etc).
   Lo usa: Service. NO lo usa directamente el Controller.
---------------------------------------------------------------------------------------------
*/
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    boolean existsByCorreoIgnoreCase(String correo);
 // NUEVOS (para verificar duplicado excluyendo el propio id)
    boolean existsByDniAndIdNot(String dni, Integer id);
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Integer id);
}

