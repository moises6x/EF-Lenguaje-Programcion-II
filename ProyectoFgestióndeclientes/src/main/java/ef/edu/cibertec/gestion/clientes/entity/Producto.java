package ef.edu.cibertec.gestion.clientes.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Producto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @Lob
    private String descripcion;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @Min(0)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EstadoProducto estado = EstadoProducto.Activo;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detalles;
}
