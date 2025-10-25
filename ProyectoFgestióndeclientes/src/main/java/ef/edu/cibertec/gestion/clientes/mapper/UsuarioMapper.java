package ef.edu.cibertec.gestion.clientes.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ef.edu.cibertec.gestion.clientes.api.request.UsuarioRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.UsuarioResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    // Request -> Entity (crear)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaCreacion", ignore = true),
        @Mapping(target = "roles", expression = "java(mapRoleIds(dto.getRoleIds()))")
    })
    Usuario toEntity(UsuarioRequestDto dto);

    // Entity -> Response
    @Mapping(target = "roles", expression = "java(toRolSimpleList(entity.getRoles()))")
    UsuarioResponseDto toResponseDto(Usuario entity);

    // Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaCreacion", ignore = true),
        @Mapping(target = "roles",
                 expression = "java(dto.getRoleIds()==null ? entity.getRoles() : mapRoleIds(dto.getRoleIds()))")
    })
    void updateEntityFromDto(UsuarioRequestDto dto, @MappingTarget Usuario entity);

    // ===== Helpers =====
    default Set<Rol> mapRoleIds(Set<Integer> roleIds) {
        if (roleIds == null) return null;
        return roleIds.stream()
                .map(id -> Rol.builder().id(id).build())
                .collect(java.util.stream.Collectors.toSet());
    }

    default List<UsuarioResponseDto.RolSimpleDto> toRolSimpleList(Set<Rol> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(r -> UsuarioResponseDto.RolSimpleDto.builder()
                        .id(r.getId())
                        .nombreRol(r.getNombreRol())
                        .build())
                .toList();
    }

    @AfterMapping
    default void normalizeUsername(@MappingTarget Usuario entity) {
        if (entity.getUsername() != null) {
            entity.setUsername(entity.getUsername().trim().toLowerCase());
        }
    }
}

