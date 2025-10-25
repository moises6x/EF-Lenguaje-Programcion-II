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

import ef.edu.cibertec.gestion.clientes.api.request.DireccionRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.DireccionResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DireccionMapper {

    DireccionMapper INSTANCE = Mappers.getMapper(DireccionMapper.class);

    // Request → Entity
    @Mappings({
        @Mapping(target = "cliente", source = "clienteId", qualifiedByName = "mapCliente")
    })
    Direccion toEntity(DireccionRequestDto dto);

    // Entity → Response
    @Mappings({
        @Mapping(target = "clienteId", source = "cliente.id"),
        @Mapping(target = "clienteNombre", expression = "java(entity.getCliente() != null ? entity.getCliente().getNombre() + \" \" + entity.getCliente().getApellido() : null)")
    })
    DireccionResponseDto toResponseDto(Direccion entity);

    // Actualizar parcialmente (PUT)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DireccionRequestDto dto, @MappingTarget Direccion entity);

    // === Helper para mapear cliente ===
    @Named("mapCliente")
    default Cliente mapCliente(Integer id) {
        if (id == null) return null;
        return Cliente.builder().id(id).build();
    }
}

