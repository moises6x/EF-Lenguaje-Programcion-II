package ef.edu.cibertec.gestion.clientes.api.request;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NewsletterSubscriptionRequest {
    @NotBlank @Size(max=80)
    private String nombre;

    @NotBlank @Email @Size(max=120)
    private String email;

    @Size(max=20)
    private String telefono;

    private List<@Size(max=40) String> intereses;
}