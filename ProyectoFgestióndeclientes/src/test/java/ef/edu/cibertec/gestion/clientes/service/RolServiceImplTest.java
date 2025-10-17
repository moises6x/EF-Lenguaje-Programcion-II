package ef.edu.cibertec.gestion.clientes.service;


import ef.edu.cibertec.gestion.clientes.entity.Rol;

import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.RolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RolServiceImplTest {
	
	
	@Mock
    private RolRepository rolRepo;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rolBase = new Rol();
        rolBase.setId(1);
        rolBase.setNombreRol("Admin");
        rolBase.setDescripcion("Rol de administrador");
        rolBase.setUsuarios(null); // Ajusta según tu entidad si incluye relación con Usuario
    }

    // 🟢 CREAR ROL
    @Test
    @DisplayName("✅ Crear rol correctamente")
    void shouldCreateRolSuccessfully() {
        when(rolRepo.save(any(Rol.class))).thenReturn(rolBase);

        Rol creado = rolService.crearRol(rolBase);

        assertNotNull(creado);
        assertEquals("Admin", creado.getNombreRol());
        verify(rolRepo).save(any(Rol.class));
    }

    @Test
    @DisplayName("❌ No debe crear rol si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        when(rolRepo.save(any(Rol.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> rolService.crearRol(rolBase));
        verify(rolRepo).save(any(Rol.class));
    }

    // 🔵 OBTENER ROL
    @Test
    @DisplayName("✅ Obtener rol por ID correctamente")
    void shouldGetRolByIdSuccessfully() {
        when(rolRepo.findById(1)).thenReturn(Optional.of(rolBase));

        Rol result = rolService.obtenerRolPorId(1);

        assertEquals("Admin", result.getNombreRol());
    }

    @Test
    @DisplayName("❌ Error al obtener rol inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentRol() {
        when(rolRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> rolService.obtenerRolPorId(999));
    }

    // 🟡 ACTUALIZAR ROL
    @Test
    @DisplayName("✅ Actualizar rol correctamente")
    void shouldUpdateRolSuccessfully() {
        when(rolRepo.findById(1)).thenReturn(Optional.of(rolBase));
        when(rolRepo.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

        Rol cambios = new Rol();
        cambios.setNombreRol("Admin Modificado");
        cambios.setDescripcion("Rol de administrador modificado");

        Rol actualizado = rolService.actualizarRol(1, cambios);

        assertEquals("Admin Modificado", actualizado.getNombreRol());
        assertEquals("Rol de administrador modificado", actualizado.getDescripcion());
    }

    @Test
    @DisplayName("❌ Error al actualizar rol inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(rolRepo.findById(999)).thenReturn(Optional.empty());
        Rol cambios = new Rol();
        assertThrows(NotFoundException.class, () -> rolService.actualizarRol(999, cambios));
    }

    // 🔴 ELIMINAR ROL
    @Test
    @DisplayName("✅ Eliminar rol correctamente")
    void shouldDeleteRolSuccessfully() {
        when(rolRepo.findById(1)).thenReturn(Optional.of(rolBase));
        doNothing().when(rolRepo).deleteById(1);

        rolService.eliminarRol(1);

        verify(rolRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar rol inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(rolRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> rolService.eliminarRol(999));
    }

    // 🟣 LISTAR ROLES
    @Test
    @DisplayName("✅ Listar todos los roles")
    void shouldListAllRoles() {
        when(rolRepo.findAll()).thenReturn(List.of(rolBase));

        List<Rol> roles = rolService.listarRoles();

        assertEquals(1, roles.size());
        assertEquals("Admin", roles.get(0).getNombreRol());
    }

    // 🟣 BUSCAR POR NOMBRE
    @Test
    @DisplayName("✅ Buscar rol por nombre correctamente")
    void shouldFindRolByNombre() {
        when(rolRepo.findByNombreRolIgnoreCase("admin")).thenReturn(Optional.of(rolBase));

        Rol result = rolService.buscarPorNombre("admin");

        assertNotNull(result);
        assertEquals("Admin", result.getNombreRol());
    }

    @Test
    @DisplayName("❌ No encontrar rol por nombre inexistente")
    void shouldReturnNullWhenSearchingNonexistentRol() {
        when(rolRepo.findByNombreRolIgnoreCase("noexiste")).thenReturn(Optional.empty());

        Rol result = rolService.buscarPorNombre("noexiste");

        assertNull(result);
    }
	

}
