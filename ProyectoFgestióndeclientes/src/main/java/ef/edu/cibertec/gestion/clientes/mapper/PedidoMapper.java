package ef.edu.cibertec.gestion.clientes.mapper;

import java.util.List;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.PedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.PedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    // ===== Request -> Entity =====
    @Mappings({
        @Mapping(target = "cliente", source = "clienteId", qualifiedByName = "clienteRef"),
        @Mapping(target = "estado", expression = "java( mapEstado(dto.getEstado()) )"),
        // mapear detalles
        @Mapping(target = "detalles", source = "detalles", qualifiedByName = "itemsToDetalles")
    })
    Pedido toEntity(PedidoRequestDto dto);

    // ===== Entity -> Response =====
    @Mappings({
        @Mapping(target = "clienteId", source = "cliente.id"),
        @Mapping(target = "clienteNombre",
                 expression = "java( entity.getCliente()!=null ? entity.getCliente().getNombre()+\" \"+entity.getCliente().getApellido() : null)"),
        @Mapping(target = "estado", expression = "java( entity.getEstado()!=null ? entity.getEstado().name() : null)"),
        @Mapping(target = "detalles", source = "detalles", qualifiedByName = "detallesToItems")
    })
    PedidoResponseDto toResponseDto(Pedido entity);

    List<PedidoResponseDto> toResponseList(List<Pedido> entities);

    // ====== helpers ======
    @Named("clienteRef")
    default Cliente clienteRef(Integer id) {
        if (id == null) return null;
        return Cliente.builder().id(id).build();
    }

    default EstadoPedido mapEstado(String e) {
        if (e == null) return null;
        try { return EstadoPedido.valueOf(e); }
        catch (IllegalArgumentException ex) { return null; }
    }

    // Request.Item -> DetallePedido
    @Named("itemsToDetalles")
    default List<DetallePedido> itemsToDetalles(List<PedidoRequestDto.ItemRequest> items) {
        if (items == null) return null;
        return items.stream().map(it ->
            DetallePedido.builder()
                .producto( Producto.builder().id(it.getProductoId()).build() )
                .cantidad(it.getCantidad())
                .subtotal(it.getSubtotal())
                .build()
        ).toList();
    }

    // DetallePedido -> Response.Item
    @Named("detallesToItems")
    default List<PedidoResponseDto.ItemResponse> detallesToItems(List<DetallePedido> detalles) {
        if (detalles == null) return null;
        return detalles.stream().map(d ->
            PedidoResponseDto.ItemResponse.builder()
                .id(d.getId())
                .productoId(d.getProducto()!=null ? d.getProducto().getId() : null)
                .productoNombre(d.getProducto()!=null ? d.getProducto().getNombre() : null)
                .cantidad(d.getCantidad())
                .subtotal(d.getSubtotal())
                .build()
        ).toList();
    }

    // update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "cliente", source = "clienteId", qualifiedByName = "clienteRef"),
        @Mapping(target = "estado", expression = "java( mapEstado(dto.getEstado()) )"),
        @Mapping(target = "detalles", source = "detalles", qualifiedByName = "itemsToDetalles")
    })
    void updateEntityFromDto(PedidoRequestDto dto, @MappingTarget Pedido entity);

    @AfterMapping
    default void setBackrefs(@MappingTarget Pedido pedido) {
        if (pedido.getDetalles() != null) {
            pedido.getDetalles().forEach(d -> d.setPedido(pedido));
        }
    }
}
