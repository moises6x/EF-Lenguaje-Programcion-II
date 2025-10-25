
package ef.edu.cibertec.gestion.clientes.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.ProductoRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.ProductoResponseDto; // <- ESTE IMPORT
import ef.edu.cibertec.gestion.clientes.entity.EstadoProducto;
import ef.edu.cibertec.gestion.clientes.entity.Producto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapper {

    ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "detalles", ignore = true),
        @Mapping(target = "estado", expression = "java(mapEstadoProducto(dto.getEstado()))")
    })
    Producto toEntity(ProductoRequestDto dto);

    @Mapping(target = "estado", expression = "java(mapEstadoProductoToString(entity.getEstado()))")
    ProductoResponseDto toResponseDto(Producto entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "detalles", ignore = true),
        @Mapping(target = "estado",
                 expression = "java(dto.getEstado()==null ? entity.getEstado() : mapEstadoProducto(dto.getEstado()))")
    })
    void updateEntityFromDto(ProductoRequestDto dto, @MappingTarget Producto entity);

    // Helpers
    default EstadoProducto mapEstadoProducto(String e) {
        if (e == null) return null;
        try { return EstadoProducto.valueOf(e); } catch (IllegalArgumentException ex) { return null; }
    }
    default String mapEstadoProductoToString(EstadoProducto e) {
        return e != null ? e.name() : null;
    }
}

