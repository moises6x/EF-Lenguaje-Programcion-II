package ef.edu.cibertec.gestion.clientes.api.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolResponseDto {
    private Integer id;
    private String nombreRol;
    private String descripcion;

    // Conveniente para la UI (cuántos usuarios tienen este rol)
    private Integer usuariosCount;
}

