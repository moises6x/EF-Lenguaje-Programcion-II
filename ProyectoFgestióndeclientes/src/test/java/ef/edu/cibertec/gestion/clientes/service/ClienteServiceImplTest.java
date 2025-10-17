package ef.edu.cibertec.gestion.clientes.service;

import ef.edu.cibertec.gestion.clientes.entity.Cliente;
import ef.edu.cibertec.gestion.clientes.exception.BusinessException;
import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.ClienteRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.ClienteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ TEST UNITARIO COMPLETO DEL CRUD DE ClienteServiceImpl  este test no modifica la base de datos real 
 */
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepo;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteBase = new Cliente();
        clienteBase.setId(1);
        clienteBase.setNombre("Juan");
        clienteBase.setApellido("Pérez");
        clienteBase.setCorreo("juan.perez@example.com");
        clienteBase.setDni("12345678");
        clienteBase.setTelefono("999888777");
        clienteBase.setFechaRegistro(LocalDateTime.now());
    }

    // 🟢 CREAR CLIENTE
    @Test
    @DisplayName("✅ Crear cliente correctamente")
    void shouldCreateClienteSuccessfully() {
        when(clienteRepo.findByDni("12345678")).thenReturn(Optional.empty());
        when(clienteRepo.existsByCorreoIgnoreCase("juan.perez@example.com")).thenReturn(false);
        when(clienteRepo.save(any(Cliente.class))).thenReturn(clienteBase);

        Cliente creado = clienteService.crear(clienteBase);

        assertNotNull(creado);
        assertEquals("Juan", creado.getNombre());
        verify(clienteRepo).save(any(Cliente.class));
    }

    @Test
    @DisplayName("❌ No debe crear cliente si el DNI ya existe")
    void shouldThrowBusinessExceptionWhenDniExists() {
        when(clienteRepo.findByDni("12345678")).thenReturn(Optional.of(clienteBase));

        assertThrows(BusinessException.class, () -> clienteService.crear(clienteBase));
        verify(clienteRepo, never()).save(any());
    }

    // 🟡 ACTUALIZAR CLIENTE
    @Test
    @DisplayName("✅ Actualizar cliente correctamente")
    void shouldUpdateClienteSuccessfully() {
        when(clienteRepo.findById(1)).thenReturn(Optional.of(clienteBase));
        when(clienteRepo.existsByCorreoIgnoreCase(anyString())).thenReturn(false);
        when(clienteRepo.findByDni(anyString())).thenReturn(Optional.empty());
        when(clienteRepo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente cambios = new Cliente();
        cambios.setNombre("Juan Carlos");
        cambios.setCorreo("nuevo@example.com");

        Cliente actualizado = clienteService.actualizar(1, cambios);

        assertEquals("Juan Carlos", actualizado.getNombre());
        assertEquals("nuevo@example.com", actualizado.getCorreo());
    }

    @Test
    @DisplayName("❌ Error al actualizar cliente inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(clienteRepo.findById(999)).thenReturn(Optional.empty());
        Cliente cambios = new Cliente();
        assertThrows(NotFoundException.class, () -> clienteService.actualizar(999, cambios));
    }

    // 🔴 ELIMINAR CLIENTE
    @Test
    @DisplayName("✅ Eliminar cliente correctamente")
    void shouldDeleteClienteSuccessfully() {
        when(clienteRepo.findById(1)).thenReturn(Optional.of(clienteBase));
        doNothing().when(clienteRepo).delete(clienteBase);

        clienteService.eliminar(1);

        verify(clienteRepo).delete(clienteBase);
    }

    @Test
    @DisplayName("❌ Error al eliminar cliente inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(clienteRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clienteService.eliminar(999));
    }

    // 🔵 OBTENER CLIENTE
    @Test
    @DisplayName("✅ Obtener cliente por ID correctamente")
    void shouldGetClienteByIdSuccessfully() {
        when(clienteRepo.findById(1)).thenReturn(Optional.of(clienteBase));

        Cliente result = clienteService.obtener(1);

        assertEquals("Juan", result.getNombre());
    }

    @Test
    @DisplayName("❌ Error al obtener cliente inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentCliente() {
        when(clienteRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> clienteService.obtener(999));
    }

    // 🟣 LISTAR CLIENTES
    @Test
    @DisplayName("✅ Listar todos los clientes")
    void shouldListAllClientes() {
        when(clienteRepo.findAll()).thenReturn(List.of(clienteBase));

        List<Cliente> clientes = clienteService.listar();

        assertEquals(1, clientes.size());
        assertEquals("Juan", clientes.get(0).getNombre());
    }

    // 🟠 BUSCAR POR DNI
    @Test
    @DisplayName("✅ Buscar cliente por DNI")
    void shouldFindClienteByDni() {
        when(clienteRepo.findByDni("12345678")).thenReturn(Optional.of(clienteBase));

        Optional<Cliente> result = clienteService.buscarPorDni("12345678");

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
    }
}
