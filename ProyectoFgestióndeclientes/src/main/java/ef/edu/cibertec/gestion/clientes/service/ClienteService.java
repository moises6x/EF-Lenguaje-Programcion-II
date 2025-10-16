package ef.edu.cibertec.gestion.clientes.service;

import java.util.List;
import java.util.Optional;
import ef.edu.cibertec.gestion.clientes.entity.Cliente;

public interface ClienteService {

    Cliente crear(Cliente cliente);

    Cliente actualizar(Integer id, Cliente cambios);

    void eliminar(Integer id);

    Cliente obtener(Integer id); // 👈 Cambia este nombre (antes era obtenerPorId)

    List<Cliente> listar();

    Optional<Cliente> buscarPorDni(String dni);
}
