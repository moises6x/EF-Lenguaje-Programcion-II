package ef.edu.cibertec.gestion.clientes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    List<Direccion> findByClienteId(Integer idCliente);
}
