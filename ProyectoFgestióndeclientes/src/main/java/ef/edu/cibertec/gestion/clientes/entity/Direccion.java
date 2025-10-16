package ef.edu.cibertec.gestion.clientes.entity;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Direccion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Direccion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String direccion;

    @Size(max = 100)
    private String ciudad;

    @Size(max = 100)
    private String provincia;

    @Size(max = 20)
    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Size(max = 100)
    private String pais = "Perú";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false,
        foreignKey = @ForeignKey(name = "fk_direccion_cliente"))
    private Cliente cliente;
}
