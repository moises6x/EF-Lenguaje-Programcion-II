package ef.edu.cibertec.gestion.clientes.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolRequestDto {

    @NotBlank
    @Size(max = 50)
    private String nombreRol;

    @Size(max = 255)
    private String descripcion;
}

