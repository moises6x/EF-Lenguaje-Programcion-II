package ef.edu.cibertec.gestion.clientes.api.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDto {

    @NotBlank @Size(max = 120)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio no puede ser negativo")
    @Digits(integer = 9, fraction = 2)
    private BigDecimal precio;

    @NotNull
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    /**
     * Estado como texto para el front: "Activo" | "Inactivo".
     * Se convierte a Enum en el mapper.
     */
    @NotBlank
    private String estado;
}
