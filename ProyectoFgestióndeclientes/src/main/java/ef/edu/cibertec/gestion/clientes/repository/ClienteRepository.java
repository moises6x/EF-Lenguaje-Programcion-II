package ef.edu.cibertec.gestion.clientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ef.edu.cibertec.gestion.clientes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    boolean existsByCorreoIgnoreCase(String correo);
}

