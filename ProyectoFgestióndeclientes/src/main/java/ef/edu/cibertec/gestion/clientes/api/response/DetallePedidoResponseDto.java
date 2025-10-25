package ef.edu.cibertec.gestion.clientes.api.response;



import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoResponseDto {

    private Integer id;
    private Integer pedidoId;
    private Integer productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal subtotal;
}
