package ef.edu.cibertec.gestion.clientes.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import ef.edu.cibertec.gestion.clientes.service.RolService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class RolServiceImpl implements RolService {
	private final RolRepository repository;

    @Override
    public List<Rol> listarRoles() {
        return repository.findAll();
    }

    @Override
    public Rol obtenerRolPorId(Integer id) {
        Optional<Rol> rol = repository.findById(id);
        return rol.orElse(null);
    }

    @Override
    public Rol crearRol(Rol rol) {
        return repository.save(rol);
    }

    @Override
    public Rol actualizarRol(Integer id, Rol rol) {
        Optional<Rol> rolExistente = repository.findById(id);
        if (rolExistente.isPresent()) {
            Rol existing = rolExistente.get();
            existing.setNombreRol(rol.getNombreRol());
            existing.setDescripcion(rol.getDescripcion());
            return repository.save(existing);
        }
        return null;
    }

    @Override
    public void eliminarRol(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Rol buscarPorNombre(String nombre) {
        Optional<Rol> rol = repository.findByNombreRolIgnoreCase(nombre);
        return rol.orElse(null);
    }
}
