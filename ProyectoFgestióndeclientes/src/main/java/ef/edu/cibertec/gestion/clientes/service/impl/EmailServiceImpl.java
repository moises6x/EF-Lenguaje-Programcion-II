package ef.edu.cibertec.gestion.clientes.service.impl;

import ef.edu.cibertec.gestion.clientes.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:MiTienda <no-reply@mitienda.local>}")
    private String from;

    @Override
    public void sendWelcomeEmail(String to, String nombre, String[] intereses) {
        String subject = "¡Bienvenido/a a MiTienda!";
        String ints = (intereses != null && intereses.length > 0)
                ? String.join(", ", intereses) : "tus intereses";

        String text = """
                Hola %s,

                ¡Gracias por suscribirte a MiTienda! A partir de ahora recibirás ofertas y novedades
                personalizadas según %s.

                Si en algún momento no deseas recibir más correos, podrás darte de baja desde el enlace del pie.

                — Equipo MiTienda
                """.formatted(nombre, ints);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);       // opcional si ya definiste un remitente por defecto en Gmail
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        mailSender.send(msg);
        log.info("Email de bienvenida (texto) enviado a {}", to);
    }
}
