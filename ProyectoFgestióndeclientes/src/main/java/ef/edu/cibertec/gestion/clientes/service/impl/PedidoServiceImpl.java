package ef.edu.cibertec.gestion.clientes.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.PedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.PedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Pedido;
import ef.edu.cibertec.gestion.clientes.mapper.PedidoMapper;
import ef.edu.cibertec.gestion.clientes.repository.PedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.PedidoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final PedidoMapper mapper;

    @Override
    public PedidoResponseDto crear(PedidoRequestDto request) {
        Pedido entity = mapper.toEntity(request);
        if (entity.getFechaPedido() == null) entity.setFechaPedido(LocalDateTime.now());
        Pedido saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override @Transactional(readOnly = true)
    public List<PedidoResponseDto> listar() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override @Transactional(readOnly = true)
    public PedidoResponseDto obtener(Integer id) {
        Pedido p = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        return mapper.toResponseDto(p);
    }

    @Override @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPorClienteId(Integer idCliente) {
        return repository.findByClienteId(idCliente).stream()
                .map(mapper::toResponseDto).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPorEstado(String estado) {
        return repository.findByEstado(estado).stream()
                .map(mapper::toResponseDto).toList();
    }

    @Override
    public PedidoResponseDto actualizar(Integer id, PedidoRequestDto request) {
        Pedido actual = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        mapper.updateEntityFromDto(request, actual);
        Pedido saved = repository.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}

