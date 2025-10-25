package ef.edu.cibertec.gestion.clientes.entity;

import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.*;

import jakarta.validation.constraints.*;

import lombok.*;

/* -----------------------------------------------------------------------------
   ¿Quién mapea tu clase a una tabla?
   - Anotaciones de JPA (jakarta.persistence.*). 
   - Spring Boot + Spring Data JPA detectan @Entity y registran el mapeo.
   - Hibernate (provider JPA) ejecuta el SQL real contra la BD usando ese mapeo.
   ¿Quién valida?
   - Bean Validation (jakarta.validation.*) valida los datos en los atributos.
   ¿Quién controla JSON?
   - Jackson (com.fasterxml.jackson.*) convierte Entity <-> JSON en el Controller.
----------------------------------------------------------------------------- */

@Entity                          // JPA: esta clase es una entidad persistente (se guarda en BD)
@Table(name = "Cliente")         // JPA: nombre de la tabla en la BD
@Getter @Setter                  // Lombok: genera getters y setters
@NoArgsConstructor               // Lombok: constructor vacío
@AllArgsConstructor              // Lombok: constructor con todos los campos
@Builder                         // Lombok: patrón builder
public class Cliente {

    // ----------------------- PK / Clave primaria -----------------------------

    @Id                                           // JPA: clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // JPA: la BD genera el ID (auto-increment). En MySQL es el clásico AUTO_INCREMENT.
    @Column(name = "id_cliente")                  // JPA: nombre de la columna
    private Integer id;

    // ----------------------- Campos simples ----------------------------------

    @NotBlank                                     // Bean Validation: no permitir vacío o solo espacios
    @Size(max = 100)                              // Bean Validation: largo máximo
    private String nombre;                        // JPA: columna implícita "nombre" (por convención)

    @NotBlank
    @Size(max = 100)
    private String apellido;

    @NotBlank
    @Size(max = 15)
    @Column(unique = true)                        // JPA: índice único en la columna
    private String dni;

    @Size(max = 20)
    private String telefono;

    @Email                                        // Bean Validation: debe tener formato de email
    @Size(max = 100)
    @Column(unique = true)                        // JPA: índice único para evitar correos duplicados
    private String correo;

    @Column(name = "fecha_registro",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    // JPA: nombre y tipo de columna; el DEFAULT lo aplica la BD (útil si insertas sin fecha)
    private LocalDateTime fechaRegistro;

    // ----------------------- Relaciones --------------------------------------

    @OneToMany(
        mappedBy = "cliente",                     // JPA: lado inverso; el FK está en Direccion.cliente
        cascade = CascadeType.ALL,                // JPA: propaga persist/merge/remove a las direcciones
        orphanRemoval = true                      // JPA: si quitas una dirección de la lista, se borra en BD
    )
    @JsonManagedReference("cliente-direccion")
    /* Jackson: marca el "lado padre" de la relación para evitar ciclos al serializar a JSON.
       El lado hijo (en Direccion) usará @JsonBackReference("cliente-direccion"). */
    private List<Direccion> direcciones;

    @OneToMany(
        mappedBy = "cliente",                     // JPA: lado inverso; el FK está en Pedido.cliente
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference("cliente-pedido")
    private List<Pedido> pedidos;

    /* -------------------------------------------------------------------------
       RESUMEN DE ROLES / LIBRERÍAS:
       - JPA (jakarta.persistence.*): define el mapeo OO → Tablas/Columnas/Relaciones.
         * @Entity, @Table, @Id, @GeneratedValue, @Column, @OneToMany, etc.
       - Hibernate: implementación JPA que ejecuta SQL real (insert/select/update/delete).
       - Bean Validation (jakarta.validation.*): valida datos (ej. @NotBlank, @Email, @Size).
       - Jackson: serializa/deserializa JSON en los controladores; @JsonManagedReference/@JsonBackReference
                  evitan recursión infinita en relaciones bidireccionales.
       - Lombok: reduce boilerplate (getters/setters/constructores/builder) en compilación.

       ¿Cuándo ocurre todo?
       - Al levantar Spring Boot, escanea @Entity y crea el esquema (según ddl-auto) con Hibernate.
       - Cuando llamas a un Repository (Spring Data JPA), Hibernate usa este mapeo para armar queries.
       - Cuando devuelves un objeto desde un @RestController, Jackson lo convierte a JSON.
       - Si @Valid está en tu Controller/Service, Bean Validation valida antes de persistir.
    ------------------------------------------------------------------------- */
}

