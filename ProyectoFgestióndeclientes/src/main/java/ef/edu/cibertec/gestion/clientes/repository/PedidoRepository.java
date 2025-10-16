package ef.edu.cibertec.gestion.clientes.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ef.edu.cibertec.gestion.clientes.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByClienteId(Integer idCliente);
    List<Pedido> findByEstado(String estado);
}
