package ef.edu.cibertec.gestion.clientes.api.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {
    private Integer id;
    private String username;
    private LocalDateTime fechaCreacion;

    private List<RolSimpleDto> roles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RolSimpleDto {
        private Integer id;
        private String nombreRol;
    }
}
