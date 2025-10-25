package ef.edu.cibertec.gestion.clientes.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.ProductoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.ProductoResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.EstadoProducto;
import ef.edu.cibertec.gestion.clientes.entity.Producto;
import ef.edu.cibertec.gestion.clientes.mapper.ProductoMapper;
import ef.edu.cibertec.gestion.clientes.repository.ProductoRepository;
import ef.edu.cibertec.gestion.clientes.service.ProductoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto request) {
        Producto entity = mapper.toEntity(request);
        Producto saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override @Transactional(readOnly = true)
    public List<ProductoResponseDto> listar() {
        return repository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Override @Transactional(readOnly = true)
    public ProductoResponseDto obtener(Integer id) {
        Producto p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return mapper.toResponseDto(p);
    }

    @Override
    public ProductoResponseDto actualizar(Integer id, ProductoRequestDto request) {
        Producto actual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        mapper.updateEntityFromDto(request, actual);
        Producto saved = repository.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarActivos() {
        return repository.findByEstado(EstadoProducto.Activo).stream()
                .map(mapper::toResponseDto).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return repository.findByRangoPrecio(min, max).stream()
                .map(mapper::toResponseDto).toList();
    }
}
