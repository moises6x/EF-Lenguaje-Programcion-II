package ef.edu.cibertec.gestion.clientes.service;

public interface EmailService {
    void sendWelcomeEmail(String to, String nombre, String[] intereses);
}