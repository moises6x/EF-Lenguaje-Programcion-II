
package ef.edu.cibertec.gestion.clientes.api.response;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDto {
    private Integer id;          // <- NECESARIO para getId()
    private String  nombre;
    private String  descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String  estado;      // "Activo" | "Inactivo"
}
