package ef.edu.cibertec.gestion.clientes.service.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;
import ef.edu.cibertec.gestion.clientes.repository.DireccionRepository;
import ef.edu.cibertec.gestion.clientes.service.DireccionService;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class DireccionServiceImpl implements DireccionService{
	
	private final DireccionRepository repository;

    @Override
    public Direccion crear(Direccion direccion) {
        return repository.save(direccion);
    }

    @Override
    public List<Direccion> listar() {
        return repository.findAll();
    }

    @Override
    public Direccion obtenerPorId(Integer id) {
        Optional<Direccion> direccion = repository.findById(id);
        return direccion.orElse(null);
    }

    @Override
    public List<Direccion> listarPorClienteId(Integer idCliente) {
        return repository.findByClienteId(idCliente);
    }

    @Override
    public Direccion actualizar(Integer id, Direccion cambios) {
        Optional<Direccion> direccionExistente = repository.findById(id);
        if (direccionExistente.isPresent()) {
            Direccion direccion = direccionExistente.get();
            direccion.setDireccion(cambios.getDireccion());
            direccion.setCiudad(cambios.getCiudad());
            direccion.setProvincia(cambios.getProvincia());
            direccion.setCodigoPostal(cambios.getCodigoPostal());
            direccion.setPais(cambios.getPais());
            return repository.save(direccion);
        }
        return null;
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
	

}
