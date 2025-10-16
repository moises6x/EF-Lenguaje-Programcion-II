package ef.edu.cibertec.gestion.clientes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedidoId(Integer idPedido);
}
