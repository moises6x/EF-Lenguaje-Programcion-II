package ef.edu.cibertec.gestion.clientes.api.request;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.*;

// Este DTO es solo para actualizaciones (PUT/PATCH)
// Nota la ausencia de @NotBlank en nombre, apellido, dni
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteUpdateDto {

    @Size(max = 100) // Se valida el tamaño SI se envía
    private String nombre;

    @Size(max = 100)
    private String apellido;

    @Size(max = 15)
    private String dni;

    @Size(max = 20)
    private String telefono;

    @Email // Se valida el formato SI se envía
    @Size(max = 100)
    private String correo;

    // (Opcional) Aún podrías querer actualizar direcciones
    private List<ClienteRequestDto.DireccionSimpleDto> direcciones; 
    // ^ Puedes reusar la clase interna si quieres
}