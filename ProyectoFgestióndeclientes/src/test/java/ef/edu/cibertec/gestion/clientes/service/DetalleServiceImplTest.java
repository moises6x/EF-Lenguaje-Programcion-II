package ef.edu.cibertec.gestion.clientes.service;

import ef.edu.cibertec.gestion.clientes.entity.DetallePedido;
import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.DetallePedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.DetalleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class DetalleServiceImplTest {
	
	@Mock
    private DetallePedidoRepository detalleRepo;

    @InjectMocks
    private DetalleServiceImpl detalleService;

    private DetallePedido detalleBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        detalleBase = new DetallePedido();
        detalleBase.setId(1);
        detalleBase.setPedido(null); // Ajusta según tu entidad
        detalleBase.setProducto(null); // Ajusta según tu entidad
        detalleBase.setCantidad(2);
        // Eliminé setFechaCreacion porque no está en la entidad
    }

    // 🟢 CREAR DETALLE
    @Test
    @DisplayName("✅ Crear detalle correctamente")
    void shouldCreateDetalleSuccessfully() {
        when(detalleRepo.save(any(DetallePedido.class))).thenReturn(detalleBase);

        DetallePedido creado = detalleService.crear(detalleBase);

        assertNotNull(creado);
        assertEquals(2, creado.getCantidad());
        verify(detalleRepo).save(any(DetallePedido.class));
    }

    @Test
    @DisplayName("❌ No debe crear detalle si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        when(detalleRepo.save(any(DetallePedido.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> detalleService.crear(detalleBase));
        verify(detalleRepo).save(any(DetallePedido.class));
    }

    // 🟡 ACTUALIZAR DETALLE
    @Test
    @DisplayName("✅ Actualizar detalle correctamente")
    void shouldUpdateDetalleSuccessfully() {
        when(detalleRepo.findById(1)).thenReturn(Optional.of(detalleBase));
        when(detalleRepo.save(any(DetallePedido.class))).thenAnswer(i -> i.getArgument(0));

        DetallePedido cambios = new DetallePedido();
        cambios.setCantidad(5);

        DetallePedido actualizado = detalleService.actualizar(1, cambios);

        assertEquals(5, actualizado.getCantidad());
    }

    @Test
    @DisplayName("❌ Error al actualizar detalle inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(detalleRepo.findById(999)).thenReturn(Optional.empty());
        DetallePedido cambios = new DetallePedido();
        assertThrows(NotFoundException.class, () -> detalleService.actualizar(999, cambios));
    }

    // 🔴 ELIMINAR DETALLE
    @Test
    @DisplayName("✅ Eliminar detalle correctamente")
    void shouldDeleteDetalleSuccessfully() {
        when(detalleRepo.findById(1)).thenReturn(Optional.of(detalleBase));
        doNothing().when(detalleRepo).deleteById(1);

        detalleService.eliminar(1);

        verify(detalleRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar detalle inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(detalleRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> detalleService.eliminar(999));
    }

    // 🔵 OBTENER DETALLE
    @Test
    @DisplayName("✅ Obtener detalle por ID correctamente")
    void shouldGetDetalleByIdSuccessfully() {
        when(detalleRepo.findById(1)).thenReturn(Optional.of(detalleBase));

        DetallePedido result = detalleService.obtenerPorId(1);

        assertEquals(2, result.getCantidad());
    }

    @Test
    @DisplayName("❌ Error al obtener detalle inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentDetalle() {
        when(detalleRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> detalleService.obtenerPorId(999));
    }

    // 🟣 LISTAR DETALLES
    @Test
    @DisplayName("✅ Listar todos los detalles")
    void shouldListAllDetalles() {
        when(detalleRepo.findAll()).thenReturn(List.of(detalleBase));

        List<DetallePedido> detalles = detalleService.listar();

        assertEquals(1, detalles.size());
        assertEquals(2, detalles.get(0).getCantidad());
    }

    // 🟠 LISTAR POR PEDIDO ID
    @Test
    @DisplayName("✅ Listar detalles por pedido ID")
    void shouldListDetallesByPedidoId() {
        when(detalleRepo.findByPedidoId(1)).thenReturn(List.of(detalleBase));

        List<DetallePedido> detalles = detalleService.listarPorPedidoId(1);

        assertEquals(1, detalles.size());
        assertEquals(2, detalles.get(0).getCantidad());
    }

}
