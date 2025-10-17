package ef.edu.cibertec.gestion.clientes.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.Pedido;
import ef.edu.cibertec.gestion.clientes.repository.PedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.PedidoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PedidoServiceImpl implements PedidoService {

	private final PedidoRepository repository;

    @Override
    public Pedido crear(Pedido pedido) {
        // Asignar fecha actual si no se proporciona
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        return repository.save(pedido);
    }

    @Override
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @Override
    public Pedido obtenerPorId(Integer id) {
        Optional<Pedido> pedido = repository.findById(id);
        return pedido.orElse(null);
    }

    @Override
    public List<Pedido> listarPorClienteId(Integer idCliente) {
        return repository.findByClienteId(idCliente);
    }

    @Override
    public List<Pedido> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    @Override
    public Pedido actualizar(Integer id, Pedido cambios) {
        Optional<Pedido> pedidoExistente = repository.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            pedido.setCliente(cambios.getCliente());
            pedido.setEstado(cambios.getEstado());
            pedido.setTotal(cambios.getTotal());
            pedido.setDetalles(cambios.getDetalles());
            return repository.save(pedido);
        }
        return null;
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}
