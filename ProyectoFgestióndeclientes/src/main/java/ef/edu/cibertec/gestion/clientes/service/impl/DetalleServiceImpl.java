package ef.edu.cibertec.gestion.clientes.service.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import ef.edu.cibertec.gestion.clientes.repository.DetallePedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.DetalleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DetalleServiceImpl implements DetalleService {
	
	
	private final DetallePedidoRepository repository;

    @Override
    public DetallePedido crear(DetallePedido detallePedido) {
        return repository.save(detallePedido);
    }

    @Override
    public List<DetallePedido> listar() {
        return repository.findAll();
    }

    @Override
    public DetallePedido obtenerPorId(Integer id) {
        Optional<DetallePedido> detalle = repository.findById(id);
        return detalle.orElse(null);
    }

    @Override
    public List<DetallePedido> listarPorPedidoId(Integer idPedido) {
        return repository.findByPedidoId(idPedido);
    }

    @Override
    public DetallePedido actualizar(Integer id, DetallePedido cambios) {
        Optional<DetallePedido> detalleExistente = repository.findById(id);
        if (detalleExistente.isPresent()) {
            DetallePedido detalle = detalleExistente.get();
            detalle.setPedido(cambios.getPedido());
            detalle.setProducto(cambios.getProducto());
            detalle.setCantidad(cambios.getCantidad());
            return repository.save(detalle);
        }
        return null;
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
	

}
