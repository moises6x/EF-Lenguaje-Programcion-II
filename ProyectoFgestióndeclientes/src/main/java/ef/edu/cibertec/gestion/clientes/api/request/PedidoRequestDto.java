package ef.edu.cibertec.gestion.clientes.api.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoRequestDto {

    @NotNull(message = "clienteId es obligatorio")
    private Integer clienteId;

    // opcional; si no viene, el service la pone con now()
    private LocalDateTime fechaPedido;

    // "Pendiente", "Pagado", "Cancelado"
    private String estado; 

    @NotNull @DecimalMin(value = "0.00")
    private BigDecimal total;

    // Detalles del pedido (producto, cantidad, subtotal)
    @Singular
    private List<ItemRequest> detalles;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ItemRequest {
        @NotNull private Integer productoId;
        @NotNull @DecimalMin("0.00") private BigDecimal subtotal;
        @NotNull(message = "cantidad es obligatoria") private Integer cantidad;
    }
}
