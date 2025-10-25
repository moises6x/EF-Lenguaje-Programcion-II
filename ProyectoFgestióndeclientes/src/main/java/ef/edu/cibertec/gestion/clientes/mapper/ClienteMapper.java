package ef.edu.cibertec.gestion.clientes.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.ClienteRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.ClienteUpdateDto; // <-- (1) IMPORTACIÓN AGREGADA
import ef.edu.cibertec.gestion.clientes.api.response.ClienteResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.entity.Direccion;

/*
---------------------------------------------------------------------------------------------
4) ClienteMapper (MAPSTRUCT) → “traductor” entre DTOs y Entidades.
   - Aísla la forma interna (Entity) de la forma pública (DTOs).
   - Si la BD cambia, ajustas aquí sin romper al Frontend.
---------------------------------------------------------------------------------------------
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    // (Opcional con componentModel="spring")
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    // ===== Request → Entity (crear) =====
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaRegistro", expression = "java(nowIfNull())"),
        @Mapping(target = "pedidos", ignore = true)
    })
    Cliente toEntity(ClienteRequestDto dto);

    // ===== Update parcial (ignora nulos del request) - Método original =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaRegistro", ignore = true),
        @Mapping(target = "pedidos", ignore = true)
    })
    void updateEntityFromDto(ClienteRequestDto dto, @MappingTarget Cliente entity);

    // ===== Entity → Response =====
    @Mappings({
        @Mapping(target = "totalPedidos",
                 expression = "java(entity.getPedidos() == null ? 0 : entity.getPedidos().size())"),
        // si quieres ser explícito con la lista de direcciones (no es obligatorio si hay método de item)
        @Mapping(target = "direcciones", source = "direcciones")
    })
    ClienteResponseDto toResponseDto(Cliente entity);

    // ==== listado (lo necesita tu Service) ====
    List<ClienteResponseDto> toResponseList(List<Cliente> entities);

    // ====== Direccion -> DireccionDto (nombres distintos) ======
    @Mappings({
      @Mapping(target = "linea1",    source = "direccion"),
      @Mapping(target = "linea2",    ignore = true),        // no existe en la entidad
      @Mapping(target = "ciudad",    source = "ciudad"),
      @Mapping(target = "distrito",  source = "provincia"),
      @Mapping(target = "zip",       source = "codigoPostal"),
      @Mapping(target = "principal", ignore = true)         // no existe en la entidad
    })
    ClienteResponseDto.DireccionDto toDireccionDto(Direccion entity);
    

    // Para mapear listas de Direccion automáticamente
    List<ClienteResponseDto.DireccionDto> toDireccionDtoList(List<Direccion> list);
 // ... (después de tu método toDireccionDtoList o donde prefieras)

    // ===== DTO (Request) -> Entity (Direccion) =====
    // ESTE ES EL MÉTODO QUE FALTA
    @Mappings({
        // Mapea los nombres que son diferentes
        @Mapping(target = "direccion",    source = "linea1"),
        @Mapping(target = "provincia",    source = "distrito"),
        @Mapping(target = "codigoPostal", source = "zip"),
        
        // Ignoramos el PK (la BD lo genera)
        // El 'id' del DTO se usa para updates, no para crear
        @Mapping(target = "id",  ignore = true), // <-- ¡CORRECTO!
        
        // El Service se encargará de asignar el cliente
        @Mapping(target = "cliente",      ignore = true) 
        
        // ciudad se mapea automáticamente porque se llama igual
    })
    Direccion toDireccionEntity(ClienteRequestDto.DireccionSimpleDto dto);
    // ====== helpers ======
    default LocalDateTime nowIfNull() {
        return LocalDateTime.now();
    }
    
    // ===== (2) NUEVO MÉTODO AGREGADO para ClienteUpdateDto =====
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaRegistro", ignore = true),
        @Mapping(target = "pedidos", ignore = true)
    })
    void updateEntityFromDto(ClienteUpdateDto dto, @MappingTarget Cliente entity);
}
