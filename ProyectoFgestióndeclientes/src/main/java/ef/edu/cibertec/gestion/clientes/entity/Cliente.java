package ef.edu.cibertec.gestion.clientes.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_cliente")
	    private Integer id;

	    @NotBlank
	    @Size(max = 100)
	    @Column(nullable = false, length = 100)
	    private String nombre;

	    @NotBlank
	    @Size(max = 100)
	    @Column(nullable = false, length = 100)
	    private String apellido;

	    @NotBlank
	    @Size(max = 15)
	    @Column(unique = true, nullable = false, length = 15)
	    private String dni;

	    @Size(max = 20)
	    private String telefono;

	    @Email
	    @Size(max = 100)
	    @Column(unique = true)
	    private String correo;

	    @Column(name = "fecha_registro", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	    private LocalDateTime fechaRegistro;

	    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Direccion> direcciones;

	    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Pedido> pedidos;
}
