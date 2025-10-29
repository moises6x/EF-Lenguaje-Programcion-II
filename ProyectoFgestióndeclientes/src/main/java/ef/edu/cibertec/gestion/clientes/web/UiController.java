package ef.edu.cibertec.gestion.clientes.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UiController {

 
    // Home (INDEX)
    @GetMapping({"/", "/index"})
    public String index() {
        log.info("UI: GET / (index)");
        return "index"; // templates/index.html
    }
    // Login (tu archivo está en templates/fragments/login.html)
    @GetMapping("/login")
    public String login() {
        log.info("UI: GET /login");
        return "fragments/login";
    }
    
    @GetMapping("/registro")
    public String registro() {
        log.info("UI: GET /registro");
        return "fragments/register"; // templates/fragments/register.html
    }


  
}

