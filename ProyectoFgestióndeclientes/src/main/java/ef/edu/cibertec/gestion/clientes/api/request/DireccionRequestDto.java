package ef.edu.cibertec.gestion.clientes.api.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionRequestDto {

    @NotBlank
    @Size(max = 255)
    private String direccion;

    @Size(max = 100)
    private String ciudad;

    @Size(max = 100)
    private String provincia;

    @Size(max = 20)
    private String codigoPostal;

    @Size(max = 100)
    private String pais;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer clienteId;
}
