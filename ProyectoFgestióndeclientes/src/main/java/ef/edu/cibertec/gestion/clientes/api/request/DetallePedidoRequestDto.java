package ef.edu.cibertec.gestion.clientes.api.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoRequestDto {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Integer pedidoId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productoId;

    @NotNull
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    @NotNull
    @Min(value = 0, message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;
}
