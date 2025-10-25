package ef.edu.cibertec.gestion.clientes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.RolRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.RolResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.mapper.RolMapper;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import ef.edu.cibertec.gestion.clientes.service.RolService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository repository;
    private final RolMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> listarRoles() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDto obtenerRolPorId(Integer id) {
        Rol rol = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));
        return mapper.toResponseDto(rol);
    }

    @Override
    public RolResponseDto crearRol(RolRequestDto request) {
        Rol entity = mapper.toEntity(request);
        Rol saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public RolResponseDto actualizarRol(Integer id, RolRequestDto request) {
        Rol actual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));
        mapper.updateEntityFromDto(request, actual);
        Rol updated = repository.save(actual);
        return mapper.toResponseDto(updated);
    }

    @Override
    public void eliminarRol(Integer id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDto buscarPorNombre(String nombre) {
        Rol rol = repository.findByNombreRolIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
        return mapper.toResponseDto(rol);
    }
}
