package ef.edu.cibertec.gestion.clientes.service;

import java.math.BigDecimal;
import java.util.List;

import ef.edu.cibertec.gestion.clientes.api.request.ProductoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.ProductoResponseDto;

public interface ProductoService {

    ProductoResponseDto crear(ProductoRequestDto request);

    List<ProductoResponseDto> listar();

    ProductoResponseDto obtener(Integer id);

    ProductoResponseDto actualizar(Integer id, ProductoRequestDto request);

    void eliminar(Integer id);

    List<ProductoResponseDto> listarActivos();

    List<ProductoResponseDto> listarPorRangoPrecio(BigDecimal min, BigDecimal max);
}
