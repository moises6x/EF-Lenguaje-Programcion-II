package ef.edu.cibertec.gestion.clientes.service;


import ef.edu.cibertec.gestion.clientes.entity.Direccion;
import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.DireccionRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.DireccionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DireccionServiceImplTest {
	
	@Mock
    private DireccionRepository direccionRepo;

    @InjectMocks
    private DireccionServiceImpl direccionService;

    private Direccion direccionBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        direccionBase = new Direccion();
        direccionBase.setId(1);
        direccionBase.setDireccion("Av. Principal 123");
        direccionBase.setCiudad("Lima");
        direccionBase.setProvincia("Lima");
        direccionBase.setCodigoPostal("15001");
        direccionBase.setPais("Perú");
        direccionBase.setCliente(null); // Ajusta según tu entidad si incluye relación con Cliente
    }

    // 🟢 CREAR DIRECCIÓN
    @Test
    @DisplayName("✅ Crear dirección correctamente")
    void shouldCreateDireccionSuccessfully() {
        when(direccionRepo.save(any(Direccion.class))).thenReturn(direccionBase);

        Direccion creada = direccionService.crear(direccionBase);

        assertNotNull(creada);
        assertEquals("Av. Principal 123", creada.getDireccion());
        verify(direccionRepo).save(any(Direccion.class));
    }

    @Test
    @DisplayName("❌ No debe crear dirección si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        when(direccionRepo.save(any(Direccion.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> direccionService.crear(direccionBase));
        verify(direccionRepo).save(any(Direccion.class));
    }

    // 🟡 ACTUALIZAR DIRECCIÓN
    @Test
    @DisplayName("✅ Actualizar dirección correctamente")
    void shouldUpdateDireccionSuccessfully() {
        when(direccionRepo.findById(1)).thenReturn(Optional.of(direccionBase));
        when(direccionRepo.save(any(Direccion.class))).thenAnswer(i -> i.getArgument(0));

        Direccion cambios = new Direccion();
        cambios.setDireccion("Av. Nueva 456");
        cambios.setCiudad("Callao");

        Direccion actualizada = direccionService.actualizar(1, cambios);

        assertEquals("Av. Nueva 456", actualizada.getDireccion());
        assertEquals("Callao", actualizada.getCiudad());
    }

    @Test
    @DisplayName("❌ Error al actualizar dirección inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(direccionRepo.findById(999)).thenReturn(Optional.empty());
        Direccion cambios = new Direccion();
        assertThrows(NotFoundException.class, () -> direccionService.actualizar(999, cambios));
    }

    // 🔴 ELIMINAR DIRECCIÓN
    @Test
    @DisplayName("✅ Eliminar dirección correctamente")
    void shouldDeleteDireccionSuccessfully() {
        when(direccionRepo.findById(1)).thenReturn(Optional.of(direccionBase));
        doNothing().when(direccionRepo).deleteById(1);

        direccionService.eliminar(1);

        verify(direccionRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar dirección inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(direccionRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> direccionService.eliminar(999));
    }

    // 🔵 OBTENER DIRECCIÓN
    @Test
    @DisplayName("✅ Obtener dirección por ID correctamente")
    void shouldGetDireccionByIdSuccessfully() {
        when(direccionRepo.findById(1)).thenReturn(Optional.of(direccionBase));

        Direccion result = direccionService.obtenerPorId(1);

        assertEquals("Av. Principal 123", result.getDireccion());
    }

    @Test
    @DisplayName("❌ Error al obtener dirección inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentDireccion() {
        when(direccionRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> direccionService.obtenerPorId(999));
    }

    // 🟣 LISTAR DIRECCIONES
    @Test
    @DisplayName("✅ Listar todas las direcciones")
    void shouldListAllDirecciones() {
        when(direccionRepo.findAll()).thenReturn(List.of(direccionBase));

        List<Direccion> direcciones = direccionService.listar();

        assertEquals(1, direcciones.size());
        assertEquals("Av. Principal 123", direcciones.get(0).getDireccion());
    }

    // 🟠 LISTAR POR CLIENTE ID
    @Test
    @DisplayName("✅ Listar direcciones por cliente ID")
    void shouldListDireccionesByClienteId() {
        when(direccionRepo.findByClienteId(1)).thenReturn(List.of(direccionBase));

        List<Direccion> direcciones = direccionService.listarPorClienteId(1);

        assertEquals(1, direcciones.size());
        assertEquals("Av. Principal 123", direcciones.get(0).getDireccion());
    }

}
