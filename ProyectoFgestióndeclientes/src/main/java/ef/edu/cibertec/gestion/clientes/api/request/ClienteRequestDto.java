package ef.edu.cibertec.gestion.clientes.api.request;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.*;

/*
---------------------------------------------------------------------------------------------
2) ClienteRequestDto (REQUEST DTO) → lo que el FRONTEND ENVÍA al backend.
   - Solo contiene los campos que el UI puede mandar (no IDs generados por BD).
   - Se valida aquí con Bean Validation (@NotBlank, @Email, etc).
   - El Controller lo recibe y se lo pasa al Service.
   - El Mapper convierte ESTE DTO → Entity (Cliente) antes de guardar.
   Flujo: UI → (JSON) → Controller → Service → Mapper (Request→Entity)
   
   
   ¿Qué es el ClienteRequestDto?
Es una clase solo para ENTRADA. Define qué datos acepta tu API cuando alguien quiere crear o actualizar un cliente.
¿Por qué existe (si ya tengo la Entity)?
Porque:
La Entity representa cómo guardas los datos en la BD (incluye id, relaciones, metadatos…).
El Request DTO representa lo que el UI está autorizado a enviar (campos seguros, sin id autogenerado ni campos internos).
Separarlos te da seguridad, claridad y estabilidad del API.
---------------------------------------------------------------------------------------------
*/
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteRequestDto {

    @NotBlank @Size(max = 100)
    private String nombre;

    @NotBlank @Size(max = 100)
    private String apellido;

    @NotBlank @Size(max = 15)
    private String dni;

    @Size(max = 20)
    private String telefono;

    @Email @Size(max = 100)
    private String correo;

    // Opcional: crear/actualizar direcciones junto con el cliente
    private List<DireccionSimpleDto> direcciones;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DireccionSimpleDto {
        @NotBlank @Size(max = 150)
        private String linea1;
        @Size(max = 150)
        private String linea2;
        @Size(max = 60)
        private String ciudad;
        @Size(max = 60)
        private String distrito;
        @Size(max = 10)
        private String zip;
        private Integer id;          // útil si haces update de direcciones
        private Boolean principal;
    }
}
