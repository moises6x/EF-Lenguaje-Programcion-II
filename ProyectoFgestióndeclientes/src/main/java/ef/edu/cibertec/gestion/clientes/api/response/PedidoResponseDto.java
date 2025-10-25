package ef.edu.cibertec.gestion.clientes.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoResponseDto {
    private Integer id;
    private Integer clienteId;
    private String  clienteNombre; // "Nombre Apellido"
    private LocalDateTime fechaPedido;
    private String estado; // como texto
    private BigDecimal total;

    private List<ItemResponse> detalles;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ItemResponse {
        private Integer id;
        private Integer productoId;
        private String  productoNombre;
        private Integer cantidad;
        private BigDecimal subtotal;
    }
}

