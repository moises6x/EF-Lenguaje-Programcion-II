package ef.edu.cibertec.gestion.clientes.api.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

/*
---------------------------------------------------------------------------------------------
3) ClienteResponseDto (RESPONSE DTO) → lo que el BACKEND DEVUELVE al frontend.
   - Es el “contrato” estable del API: lo que prometes a consumidores.
   - Puede tener campos calculados/derivados (p.ej., totalPedidos).
   - El Mapper convierte Entity → ESTE DTO para exponerlo.
   Flujo: Service obtiene Entity → Mapper (Entity→Response) → Controller → UI (JSON)
---------------------------------------------------------------------------------------------
*/
@JsonInclude(JsonInclude.Include.NON_NULL)  // <- oculta campos null
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteResponseDto {

    private Integer id;             // generado por la BD
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String correo;
    private LocalDateTime fechaRegistro;

    private List<DireccionDto> direcciones; // listo para pintar en UI
    private Integer totalPedidos;           // ejemplo de campo derivado
 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DireccionDto {
        // linea2 y principal no saldrán si son null
        private Integer id;
        private String linea1;
        private String linea2;   // se ocultará si es null
        private String ciudad;
        private String distrito;
        private String zip;
        private Boolean principal; // se ocultará si es null
    }
}
