package ef.edu.cibertec.gestion.clientes.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.DetallePedidoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DetallePedidoResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import ef.edu.cibertec.gestion.clientes.entity.Pedido;
import ef.edu.cibertec.gestion.clientes.entity.Producto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DetallePedidoMapper {

    DetallePedidoMapper INSTANCE = Mappers.getMapper(DetallePedidoMapper.class);

    // Request → Entity
    @Mappings({
        @Mapping(target = "pedido", source = "pedidoId", qualifiedByName = "mapPedido"),
        @Mapping(target = "producto", source = "productoId", qualifiedByName = "mapProducto")
    })
    DetallePedido toEntity(DetallePedidoRequestDto dto);

    // Entity → Response
    @Mappings({
        @Mapping(target = "pedidoId", source = "pedido.id"),
        @Mapping(target = "productoId", source = "producto.id"),
        @Mapping(target = "productoNombre", source = "producto.nombre")
    })
    DetallePedidoResponseDto toResponseDto(DetallePedido entity);

    // Update parcial (para PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DetallePedidoRequestDto dto, @MappingTarget DetallePedido entity);

    // ==== Métodos auxiliares ====
    @Named("mapPedido")
    default Pedido mapPedido(Integer id) {
        if (id == null) return null;
        return Pedido.builder().id(id).build();
    }

    @Named("mapProducto")
    default Producto mapProducto(Integer id) {
        if (id == null) return null;
        return Producto.builder().id(id).build();
    }
}
