package ef.edu.cibertec.gestion.clientes.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.RolRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.RolResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Rol;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolMapper {

    RolMapper INSTANCE = Mappers.getMapper(RolMapper.class);

    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "usuarios", ignore = true) // no se gestiona desde este DTO
    })
    Rol toEntity(RolRequestDto dto);

    // Entity -> Response
    @Mapping(target = "usuariosCount",
             expression = "java(entity.getUsuarios() == null ? 0 : entity.getUsuarios().size())")
    RolResponseDto toResponseDto(Rol entity);

    // Update parcial (ignora nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "usuarios", ignore = true)
    })
    void updateEntityFromDto(RolRequestDto dto, @MappingTarget Rol entity);
}

