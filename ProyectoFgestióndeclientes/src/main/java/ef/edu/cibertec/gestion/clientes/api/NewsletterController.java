package ef.edu.cibertec.gestion.clientes.api;

import ef.edu.cibertec.gestion.clientes.api.request.NewsletterSubscriptionRequest;
import ef.edu.cibertec.gestion.clientes.api.response.NewsletterSubscriptionResponse;
import ef.edu.cibertec.gestion.clientes.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/newsletter")
public class NewsletterController {

    private final EmailService emailService;

    @PostMapping("/subscribe")
    public ResponseEntity<NewsletterSubscriptionResponse> subscribe(
            @Valid @RequestBody NewsletterSubscriptionRequest req) {

        String[] intereses = (req.getIntereses() == null)
                ? new String[0]
                : req.getIntereses().toArray(new String[0]);

        emailService.sendWelcomeEmail(req.getEmail(), req.getNombre(), intereses);
        log.info("Newsletter: suscripción de {} <{}>", req.getNombre(), req.getEmail());

        return ResponseEntity.ok(
                NewsletterSubscriptionResponse.builder()
                        .message("¡Gracias por suscribirte! Revisa tu correo: " + req.getEmail())
                        .build()
        );
    }
}
