package ef.edu.cibertec.gestion.clientes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.DireccionRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DireccionResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;
import ef.edu.cibertec.gestion.clientes.mapper.DireccionMapper;
import ef.edu.cibertec.gestion.clientes.repository.DireccionRepository;
import ef.edu.cibertec.gestion.clientes.service.DireccionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository repository;
    private final DireccionMapper mapper;

    @Override
    public DireccionResponseDto crear(DireccionRequestDto request) {
        Direccion entity = mapper.toEntity(request);
        Direccion saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionResponseDto> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DireccionResponseDto obtener(Integer id) {
        Direccion d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada: " + id));
        return mapper.toResponseDto(d);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionResponseDto> listarPorClienteId(Integer idCliente) {
        return repository.findByClienteId(idCliente).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public DireccionResponseDto actualizar(Integer id, DireccionRequestDto request) {
        Direccion actual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada: " + id));
        mapper.updateEntityFromDto(request, actual);
        Direccion saved = repository.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}

