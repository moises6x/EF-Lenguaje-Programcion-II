package ef.edu.cibertec.gestion.clientes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.DetallePedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DetallePedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import ef.edu.cibertec.gestion.clientes.mapper.DetallePedidoMapper;
import ef.edu.cibertec.gestion.clientes.repository.DetallePedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.DetalleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DetalleServiceImpl implements DetalleService {

    private final DetallePedidoRepository repository;
    private final DetallePedidoMapper mapper;

    @Override
    public DetallePedidoResponseDto crear(DetallePedidoRequestDto request) {
        DetallePedido entity = mapper.toEntity(request);
        DetallePedido saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoResponseDto> listar() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DetallePedidoResponseDto obtener(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetallePedidoResponseDto> listarPorPedido(Integer idPedido) {
        return repository.findByPedidoId(idPedido).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public DetallePedidoResponseDto actualizar(Integer id, DetallePedidoRequestDto request) {
        DetallePedido actual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado: " + id));

        mapper.updateEntityFromDto(request, actual);
        DetallePedido updated = repository.save(actual);

        return mapper.toResponseDto(updated);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}

