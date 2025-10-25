package ef.edu.cibertec.gestion.clientes.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
---------------------------------------------------------------------------------------------
8) UiController (WEB ) → solo si sirves VISTAS (Thymeleaf, etc.).
   - Mapea rutas de páginas (no JSON),
   - Devuelve nombres de templates (templates/*.html).
   - El Frontend JS de esas páginas consumirá la API /api/clientes y demas.
---------------------------------------------------------------------------------------------
*/
@Slf4j
@Controller
public class UiController {

    // 🏠 Página principal o lista de clientes
    @GetMapping({"/", "/clientes"})
    public String clientes() {
        log.info("UI: GET /clientes");
        return "clientes"; // → templates/clientes.html
    }


    // 📦 Listado o gestión de Detalle de Pedidos (vista pública o general)
    @GetMapping("/detalle-pedidos")
    public String detallePedidos() {
        log.info("UI: GET /detalle-pedidos");
        return "detalle-pedidos"; // → templates/detalle-pedidos.html
    }

    
    // Página principal de direcciones
    @GetMapping("/direcciones")
    public String direcciones() {
        log.info("UI: GET /direcciones");
        return "direcciones"; // → templates/direcciones.html
    }
 // /pedidos
    @GetMapping("/pedidos")
    public String pedidos() {
        log.info("UI: GET /pedidos");
        return "pedidos"; // → templates/pedidos.html
    }
    
    // Catálogo / listado público
    @GetMapping({"/", "/productos"})
    public String productos() {
        log.info("UI: GET /productos");
        return "productos"; // → templates/productos.html
    }
    
    // Vista pública/listado de Roles
    @GetMapping("/roles")
    public String roles() {
        log.info("UI: GET /roles");
        return "roles"; // → templates/roles.html
    }
    
    @GetMapping("/usuarios")
    public String usuarios() {
        log.info("UI: GET /usuarios");
        return "usuarios"; // → templates/usuarios.html
    }

}


