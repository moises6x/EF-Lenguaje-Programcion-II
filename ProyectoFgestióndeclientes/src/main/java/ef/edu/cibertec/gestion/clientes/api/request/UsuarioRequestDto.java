package ef.edu.cibertec.gestion.clientes.api.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDto {

    @NotBlank
    @Size(max = 50)
    private String username;

    // OJO: en un proyecto real, encriptar antes de persistir
    @NotBlank
    @Size(max = 255)
    private String password;

    // IDs de roles que se asignarán al usuario
    private Set<Integer> roleIds;
}
