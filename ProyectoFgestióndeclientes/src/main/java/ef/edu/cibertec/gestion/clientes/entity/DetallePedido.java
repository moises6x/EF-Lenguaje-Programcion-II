package ef.edu.cibertec.gestion.clientes.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Detalle_Pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedido {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_detalle")
	    private Integer id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "id_pedido", nullable = false,
	        foreignKey = @ForeignKey(name = "fk_detalle_pedido"))
	    private Pedido pedido;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "id_producto", nullable = false,
	        foreignKey = @ForeignKey(name = "fk_detalle_producto"))
	    private Producto producto;

	    @NotNull
	    @Min(1)
	    private Integer cantidad;

	    @NotNull
	    @DecimalMin("0.00")
	    @Column(name = "precio_unitario")
	    private BigDecimal precioUnitario;

	    @NotNull
	    @DecimalMin("0.00")
	    private BigDecimal subtotal;
}
