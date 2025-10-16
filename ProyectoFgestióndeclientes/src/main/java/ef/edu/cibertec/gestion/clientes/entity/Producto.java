package ef.edu.cibertec.gestion.clientes.entity;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Producto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("producto-detalle")
    private List<DetallePedido> detalles;
}


