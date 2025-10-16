package ef.edu.cibertec.gestion.clientes.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "Pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false,
        foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    @JsonBackReference("cliente-pedido")
    private Cliente cliente;

    @Column(name = "fecha_pedido", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoPedido estado = EstadoPedido.Pendiente;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("pedido-detalle")
    private List<DetallePedido> detalles;
}
