package ef.edu.cibertec.gestion.clientes.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.exception.BusinessException;
import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.ClienteRepository;
import ef.edu.cibertec.gestion.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepo;

    @Override
    public Cliente crear(Cliente cliente) {
        log.info("Creando cliente con DNI '{}'", cliente.getDni());

        if (clienteRepo.findByDni(cliente.getDni()).isPresent()) {
            throw new BusinessException("Ya existe un cliente con el DNI: " + cliente.getDni());
        }

        if (clienteRepo.existsByCorreoIgnoreCase(cliente.getCorreo())) {
            throw new BusinessException("Ya existe un cliente con el correo: " + cliente.getCorreo());
        }

        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }

        Cliente saved = clienteRepo.save(cliente);
        log.debug("Cliente creado con ID '{}'", saved.getId());
        return saved;
    }

    @Override
    public Cliente actualizar(Integer id, Cliente cambios) {
        log.info("Actualizando cliente ID '{}'", id);

        Cliente actual = clienteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));

        if (cambios.getNombre() != null)
            actual.setNombre(cambios.getNombre());

        if (cambios.getApellido() != null)
            actual.setApellido(cambios.getApellido());

        if (cambios.getCorreo() != null) {
            if (!cambios.getCorreo().equalsIgnoreCase(actual.getCorreo())
                    && clienteRepo.existsByCorreoIgnoreCase(cambios.getCorreo())) {
                throw new BusinessException("Ya existe un cliente con el correo: " + cambios.getCorreo());
            }
            actual.setCorreo(cambios.getCorreo());
        }

        if (cambios.getDni() != null && !cambios.getDni().equals(actual.getDni())) {
            clienteRepo.findByDni(cambios.getDni()).ifPresent(c -> {
                throw new BusinessException("Ya existe un cliente con el DNI: " + cambios.getDni());
            });
            actual.setDni(cambios.getDni());
        }

        if (cambios.getTelefono() != null)
            actual.setTelefono(cambios.getTelefono());

        if (cambios.getDirecciones() != null)
            actual.setDirecciones(cambios.getDirecciones());

 

        return clienteRepo.save(actual);
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Eliminando cliente ID '{}'", id);
        Cliente c = clienteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));
        clienteRepo.delete(c);
        log.debug("Cliente eliminado ID '{}'", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtener(Integer id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepo.findByDni(dni);
    }
}

