package ef.edu.cibertec.gestion.clientes.service;

import ef.edu.cibertec.gestion.clientes.entity.Pedido;

import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.PedidoRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoServiceImplTest {
	

	@Mock
    private PedidoRepository pedidoRepo;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedidoBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pedidoBase = new Pedido();
        pedidoBase.setId(1);
        pedidoBase.setCliente(null); // Ajusta según tu entidad si incluye relación con Cliente
        pedidoBase.setFechaPedido(LocalDateTime.now());
      
        pedidoBase.setTotal(BigDecimal.valueOf(100.00));
        pedidoBase.setDetalles(null); // Ajusta según tu entidad si incluye relación con DetallePedido
    }

    // 🟢 CREAR PEDIDO
    @Test
    @DisplayName("✅ Crear pedido correctamente con fecha automática")
    void shouldCreatePedidoSuccessfully() {
        Pedido pedidoSinFecha = new Pedido();
        pedidoSinFecha.setCliente(pedidoBase.getCliente());
        pedidoSinFecha.setEstado(pedidoBase.getEstado());
        pedidoSinFecha.setTotal(pedidoBase.getTotal());
        pedidoSinFecha.setDetalles(pedidoBase.getDetalles());

        when(pedidoRepo.save(any(Pedido.class))).thenReturn(pedidoBase);

        Pedido creado = pedidoService.crear(pedidoSinFecha);

        assertNotNull(creado);
        assertNotNull(creado.getFechaPedido()); // Verifica que se asignó la fecha
        assertEquals(BigDecimal.valueOf(100.00), creado.getTotal());
        verify(pedidoRepo).save(any(Pedido.class));
    }

    @Test
    @DisplayName("❌ No debe crear pedido si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        Pedido pedido = new Pedido();
        when(pedidoRepo.save(any(Pedido.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> pedidoService.crear(pedido));
        verify(pedidoRepo).save(any(Pedido.class));
    }

    // 🟡 ACTUALIZAR PEDIDO
    @Test
    @DisplayName("✅ Actualizar pedido correctamente")
    void shouldUpdatePedidoSuccessfully() {
        when(pedidoRepo.findById(1)).thenReturn(Optional.of(pedidoBase));
        when(pedidoRepo.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        Pedido cambios = new Pedido();
        
        cambios.setTotal(BigDecimal.valueOf(150.00));

        Pedido actualizado = pedidoService.actualizar(1, cambios);

       
        assertEquals(BigDecimal.valueOf(150.00), actualizado.getTotal());
    }

    @Test
    @DisplayName("❌ Error al actualizar pedido inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(pedidoRepo.findById(999)).thenReturn(Optional.empty());
        Pedido cambios = new Pedido();
        assertThrows(NotFoundException.class, () -> pedidoService.actualizar(999, cambios));
    }

    // 🔴 ELIMINAR PEDIDO
    @Test
    @DisplayName("✅ Eliminar pedido correctamente")
    void shouldDeletePedidoSuccessfully() {
        when(pedidoRepo.findById(1)).thenReturn(Optional.of(pedidoBase));
        doNothing().when(pedidoRepo).deleteById(1);

        pedidoService.eliminar(1);

        verify(pedidoRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar pedido inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(pedidoRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> pedidoService.eliminar(999));
    }

    // 🔵 OBTENER PEDIDO
    @Test
    @DisplayName("✅ Obtener pedido por ID correctamente")
    void shouldGetPedidoByIdSuccessfully() {
        when(pedidoRepo.findById(1)).thenReturn(Optional.of(pedidoBase));

        Pedido result = pedidoService.obtenerPorId(1);

        assertEquals(BigDecimal.valueOf(100.00), result.getTotal());
    }

    @Test
    @DisplayName("❌ Error al obtener pedido inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentPedido() {
        when(pedidoRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> pedidoService.obtenerPorId(999));
    }

    // 🟣 LISTAR PEDIDOS
    @Test
    @DisplayName("✅ Listar todos los pedidos")
    void shouldListAllPedidos() {
        when(pedidoRepo.findAll()).thenReturn(List.of(pedidoBase));

        List<Pedido> pedidos = pedidoService.listar();

        assertEquals(1, pedidos.size());
        assertEquals(BigDecimal.valueOf(100.00), pedidos.get(0).getTotal());
    }

    // 🟠 LISTAR POR CLIENTE ID
    @Test
    @DisplayName("✅ Listar pedidos por cliente ID")
    void shouldListPedidosByClienteId() {
        when(pedidoRepo.findByClienteId(1)).thenReturn(List.of(pedidoBase));

        List<Pedido> pedidos = pedidoService.listarPorClienteId(1);

        assertEquals(1, pedidos.size());
        assertEquals(BigDecimal.valueOf(100.00), pedidos.get(0).getTotal());
    }

    // 🟠 LISTAR POR ESTADO
    @Test
    @DisplayName("✅ Listar pedidos por estado")
    void shouldListPedidosByEstado() {
        when(pedidoRepo.findByEstado("Pendiente")).thenReturn(List.of(pedidoBase));

        List<Pedido> pedidos = pedidoService.listarPorEstado("Pendiente");

        assertEquals(1, pedidos.size());
        
    }
}
