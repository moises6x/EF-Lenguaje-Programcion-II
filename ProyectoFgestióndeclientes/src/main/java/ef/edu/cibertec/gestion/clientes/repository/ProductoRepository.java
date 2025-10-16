package ef.edu.cibertec.gestion.clientes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ef.edu.cibertec.gestion.clientes.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByEstado(String estado);

    @Query("SELECT p FROM Producto p WHERE p.precio >= :min AND p.precio <= :max")
    List<Producto> findByRangoPrecio(double min, double max);
}
