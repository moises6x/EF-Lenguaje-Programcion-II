package ef.edu.cibertec.gestion.clientes.api.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionResponseDto {
    private Integer id;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String pais;

    private Integer clienteId;
    private String clienteNombre; // nombre completo del cliente (para mostrar en la UI)
}
