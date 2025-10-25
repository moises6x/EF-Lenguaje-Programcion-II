package ef.edu.cibertec.gestion.clientes.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteUpdateDto;
import ef.edu.cibertec.gestion.clientes.api.response.ClienteResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.mapper.ClienteMapper;
import ef.edu.cibertec.gestion.clientes.repository.ClienteRepository;
import ef.edu.cibertec.gestion.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;

/*
---------------------------------------------------------------------------------------------
6.1) ClienteServiceImpl → Implementa la lógica:
     - Valida (únicos, formatos, etc), normaliza campos,
     - Usa Repository para leer/escribir,
     - Usa Mapper para convertir Entity ↔ DTO.
---------------------------------------------------------------------------------------------
*/
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;
    private final ClienteMapper mapper;

    @Override
    public ClienteResponseDto crear(ClienteRequestDto request) {
        
        // 1. MapStruct convierte el DTO a Entidad (incluyendo la lista de direcciones)
        Cliente entity = mapper.toEntity(request);
        
        if (entity.getFechaRegistro() == null) {
            entity.setFechaRegistro(java.time.LocalDateTime.now());
        }

        // 2. --- ¡ESTE ES EL PASO NUEVO! ---
        //    Debes establecer la relación en ambos lados (bidireccional).
        //    Le decimos a cada 'Direccion' hija quién es su 'Cliente' padre.
        if (entity.getDirecciones() != null) {
            for (ef.edu.cibertec.gestion.clientes.entity.Direccion dir : entity.getDirecciones()) {
                dir.setCliente(entity); // <-- Esto asignará el id_cliente al guardar
            }
        }
        // --- FIN DE LA CORRECCIÓN ---

        // 3. Ahora JPA guardará el cliente y (gracias a CascadeType.ALL) 
        //    guardará también las direcciones, ya que ahora tienen el id_cliente.
        Cliente saved = repo.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override @Transactional(readOnly = true)
    public List<ClienteResponseDto> listar() {
        return mapper.toResponseList(repo.findAll());
    }

    @Override @Transactional(readOnly = true)
    public ClienteResponseDto obtener(Integer id) {
        Cliente c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        return mapper.toResponseDto(c);
    }

    @Override
    public ClienteResponseDto actualizar(Integer id, ClienteUpdateDto request) {
        Cliente actual = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        // --- Validaciones de unicidad SOLO si el campo viene en el request ---
        if (request.getDni() != null &&
            repo.existsByDniAndIdNot(request.getDni(), id)) {
            throw new RuntimeException("El DNI ya está registrado por otro cliente.");
        }
        if (request.getCorreo() != null &&
            repo.existsByCorreoIgnoreCaseAndIdNot(request.getCorreo(), id)) {
            throw new RuntimeException("El correo ya está registrado por otro cliente.");
        }

        // --- Actualización parcial: MapStruct ignora nulos ---
        mapper.updateEntityFromDto(request, actual);

        Cliente saved;
        try {
            saved = repo.save(actual);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Por si la BD igual rompe por UNIQUE, devuelve un mensaje claro
            throw new RuntimeException("Violación de unicidad (dni/correo duplicado).", e);
        }
        return mapper.toResponseDto(saved);
    }


    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public ClienteResponseDto buscarPorDni(String dni) {
        Cliente c = repo.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado (dni): " + dni));
        return mapper.toResponseDto(c);
    }
}