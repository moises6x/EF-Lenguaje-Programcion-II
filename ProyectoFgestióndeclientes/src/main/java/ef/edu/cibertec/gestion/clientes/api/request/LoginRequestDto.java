package ef.edu.cibertec.gestion.clientes.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    @NotBlank private String username;
    @NotBlank private String password;
}
