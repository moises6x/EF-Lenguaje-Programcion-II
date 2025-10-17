package ef.edu.cibertec.gestion.clientes.service;
import ef.edu.cibertec.gestion.clientes.entity.Producto;

import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.ProductoRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceImplTest {
	@Mock
    private ProductoRepository productoRepo;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto productoBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productoBase = new Producto();
        productoBase.setId(1);
        productoBase.setNombre("Producto 1");
        productoBase.setDescripcion("Descripción del producto 1");
        productoBase.setPrecio(BigDecimal.valueOf(50.00));
        productoBase.setStock(10);
     
        productoBase.setDetalles(null); // Ajusta según tu entidad si incluye relación con DetallePedido
    }

    // 🟢 CREAR PRODUCTO
    @Test
    @DisplayName("✅ Crear producto correctamente")
    void shouldCreateProductoSuccessfully() {
        when(productoRepo.save(any(Producto.class))).thenReturn(productoBase);

        Producto creado = productoService.crear(productoBase);

        assertNotNull(creado);
        assertEquals("Producto 1", creado.getNombre());
        verify(productoRepo).save(any(Producto.class));
    }

    @Test
    @DisplayName("❌ No debe crear producto si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        when(productoRepo.save(any(Producto.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> productoService.crear(productoBase));
        verify(productoRepo).save(any(Producto.class));
    }

    // 🔵 OBTENER PRODUCTO
    @Test
    @DisplayName("✅ Obtener producto por ID correctamente")
    void shouldGetProductoByIdSuccessfully() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(productoBase));

        Producto result = productoService.obtenerPorId(1);

        assertEquals("Producto 1", result.getNombre());
    }

    @Test
    @DisplayName("❌ Error al obtener producto inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentProducto() {
        when(productoRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productoService.obtenerPorId(999));
    }

    // 🟡 ACTUALIZAR PRODUCTO
    @Test
    @DisplayName("✅ Actualizar producto correctamente")
    void shouldUpdateProductoSuccessfully() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(productoBase));
        when(productoRepo.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        Producto cambios = new Producto();
        cambios.setNombre("Producto Actualizado");
        cambios.setPrecio(BigDecimal.valueOf(75.00));

        Producto actualizado = productoService.actualizar(1, cambios);

        assertEquals("Producto Actualizado", actualizado.getNombre());
        assertEquals(BigDecimal.valueOf(75.00), actualizado.getPrecio());
    }

    @Test
    @DisplayName("❌ Error al actualizar producto inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(productoRepo.findById(999)).thenReturn(Optional.empty());
        Producto cambios = new Producto();
        assertThrows(NotFoundException.class, () -> productoService.actualizar(999, cambios));
    }

    // 🔴 ELIMINAR PRODUCTO
    @Test
    @DisplayName("✅ Eliminar producto correctamente")
    void shouldDeleteProductoSuccessfully() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(productoBase));
        doNothing().when(productoRepo).deleteById(1);

        productoService.eliminar(1);

        verify(productoRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar producto inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(productoRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productoService.eliminar(999));
    }

    // 🟣 LISTAR TODOS LOS PRODUCTOS
    @Test
    @DisplayName("✅ Listar todos los productos")
    void shouldListAllProductos() {
        when(productoRepo.findAll()).thenReturn(List.of(productoBase));

        List<Producto> productos = productoService.listarTodos();

        assertEquals(1, productos.size());
        assertEquals("Producto 1", productos.get(0).getNombre());
    }

    // 🟣 LISTAR PRODUCTOS ACTIVOS
    @Test
    @DisplayName("✅ Listar productos activos")
    void shouldListActivos() {
        when(productoRepo.findByEstado("Activo")).thenReturn(List.of(productoBase));

        List<Producto> productos = productoService.listarActivos();

        assertEquals(1, productos.size());
        
    }

    // 🟣 BUSCAR POR RANGO DE PRECIO
    @Test
    @DisplayName("✅ Buscar productos por rango de precio")
    void shouldFindByRangoPrecio() {
        when(productoRepo.findByRangoPrecio(40.00, 60.00)).thenReturn(List.of(productoBase));

        List<Producto> productos = productoService.findByRangoPrecio(40.00, 60.00);

        assertEquals(1, productos.size());
        assertEquals(BigDecimal.valueOf(50.00), productos.get(0).getPrecio());
    }
	

}
