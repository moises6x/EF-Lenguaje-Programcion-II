package ef.edu.cibertec.gestion.clientes.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Rol")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rol {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String nombreRol;

    @Size(max = 255)
    private String descripcion;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;
}
