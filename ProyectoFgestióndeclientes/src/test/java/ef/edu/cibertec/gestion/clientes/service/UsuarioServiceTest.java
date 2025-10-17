package ef.edu.cibertec.gestion.clientes.service;

import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;

import ef.edu.cibertec.gestion.clientes.exception.NotFoundException;
import ef.edu.cibertec.gestion.clientes.repository.UsuarioRepository;
import ef.edu.cibertec.gestion.clientes.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {
	@Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioBase = new Usuario();
        usuarioBase.setId(1);
        usuarioBase.setUsername("juanperez");
        usuarioBase.setPassword("password123");
        usuarioBase.setFechaCreacion(LocalDateTime.now());
        Set<Rol> roles = new HashSet<>();
        roles.add(new Rol(1, "Admin", "Rol de administrador", null)); // Mock básico de Rol
        usuarioBase.setRoles(roles);
    }

    // 🟢 CREAR USUARIO
    @Test
    @DisplayName("✅ Crear usuario correctamente")
    void shouldCreateUsuarioSuccessfully() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevousuario");
        nuevoUsuario.setPassword("password456");

        when(usuarioRepo.existsByUsernameIgnoreCase("nuevousuario")).thenReturn(false);
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(nuevoUsuario);

        Usuario creado = usuarioService.crearUsuario(nuevoUsuario);

        assertNotNull(creado);
        assertNotNull(creado.getFechaCreacion()); // Verifica que se asignó la fecha
        assertEquals("nuevousuario", creado.getUsername());
        verify(usuarioRepo).save(any(Usuario.class));
    }

    @Test
    @DisplayName("❌ No debe crear usuario si el username ya existe")
    void shouldReturnNullWhenUsernameExists() {
        when(usuarioRepo.existsByUsernameIgnoreCase("juanperez")).thenReturn(true);

        Usuario creado = usuarioService.crearUsuario(usuarioBase);

        assertNull(creado);
        verify(usuarioRepo, never()).save(any());
    }

    @Test
    @DisplayName("❌ No debe crear usuario si hay un error simulado")
    void shouldThrowExceptionWhenCreatingFails() {
        when(usuarioRepo.existsByUsernameIgnoreCase("nuevousuario")).thenReturn(false);
        when(usuarioRepo.save(any(Usuario.class))).thenThrow(new RuntimeException("Error al guardar"));

        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(new Usuario()));
        verify(usuarioRepo).save(any(Usuario.class));
    }

    // 🔵 OBTENER USUARIO
    @Test
    @DisplayName("✅ Obtener usuario por ID correctamente")
    void shouldGetUsuarioByIdSuccessfully() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioBase));

        Usuario result = usuarioService.obtenerUsuario(1);

        assertEquals("juanperez", result.getUsername());
    }

    @Test
    @DisplayName("❌ Error al obtener usuario inexistente")
    void shouldThrowNotFoundWhenGettingNonexistentUsuario() {
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> usuarioService.obtenerUsuario(999));
    }

    // 🟡 ACTUALIZAR USUARIO
    @Test
    @DisplayName("✅ Actualizar usuario correctamente")
    void shouldUpdateUsuarioSuccessfully() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario cambios = new Usuario();
        cambios.setUsername("juanperezmod");
        cambios.setPassword("newpassword");

        Usuario actualizado = usuarioService.actualizarUsuario(1, cambios);

        assertEquals("juanperezmod", actualizado.getUsername());
        assertEquals("newpassword", actualizado.getPassword());
    }

    @Test
    @DisplayName("❌ Error al actualizar usuario inexistente")
    void shouldThrowNotFoundWhenUpdatingNonexistent() {
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());
        Usuario cambios = new Usuario();
        assertThrows(NotFoundException.class, () -> usuarioService.actualizarUsuario(999, cambios));
    }

    // 🔴 ELIMINAR USUARIO
    @Test
    @DisplayName("✅ Eliminar usuario correctamente")
    void shouldDeleteUsuarioSuccessfully() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuarioBase));
        doNothing().when(usuarioRepo).deleteById(1);

        usuarioService.eliminarUsuario(1);

        verify(usuarioRepo).deleteById(1);
    }

    @Test
    @DisplayName("❌ Error al eliminar usuario inexistente")
    void shouldThrowNotFoundWhenDeletingNonexistent() {
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> usuarioService.eliminarUsuario(999));
    }

    // 🟣 LISTAR USUARIOS
    @Test
    @DisplayName("✅ Listar todos los usuarios")
    void shouldListAllUsuarios() {
        when(usuarioRepo.findAll()).thenReturn(List.of(usuarioBase));

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        assertEquals(1, usuarios.size());
        assertEquals("juanperez", usuarios.get(0).getUsername());
    }

    // 🟣 BUSCAR POR NOMBRE
    @Test
    @DisplayName("✅ Buscar usuario por nombre correctamente")
    void shouldFindUsuarioByNombre() {
        when(usuarioRepo.findByUsernameIgnoreCase("juanperez")).thenReturn(Optional.of(usuarioBase));

        Usuario result = usuarioService.buscarPorNombre("juanperez");

        assertNotNull(result);
        assertEquals("juanperez", result.getUsername());
    }

    @Test
    @DisplayName("❌ No encontrar usuario por nombre inexistente")
    void shouldReturnNullWhenSearchingNonexistentUsuario() {
        when(usuarioRepo.findByUsernameIgnoreCase("noexiste")).thenReturn(Optional.empty());

        Usuario result = usuarioService.buscarPorNombre("noexiste");

        assertNull(result);
    }

    // 🟣 LOGIN
    @Test
    @DisplayName("✅ Login exitoso con credenciales válidas")
    void shouldLoginSuccessfully() {
        Usuario loginRequest = new Usuario();
        loginRequest.setUsername("juanperez");
        loginRequest.setPassword("password123");

        when(usuarioRepo.findByUsernameIgnoreCase("juanperez")).thenReturn(Optional.of(usuarioBase));

        String result = usuarioService.login(loginRequest);

        assertEquals("✅ Login exitoso. Bienvenido juanperez!", result);
    }

    @Test
    @DisplayName("❌ Login fallido con credenciales inválidas")
    void shouldFailLoginWithInvalidCredentials() {
        Usuario loginRequest = new Usuario();
        loginRequest.setUsername("juanperez");
        loginRequest.setPassword("wrongpassword");

        when(usuarioRepo.findByUsernameIgnoreCase("juanperez")).thenReturn(Optional.of(usuarioBase));

        String result = usuarioService.login(loginRequest);

        assertEquals("❌ Credenciales inválidas. Verifique usuario o contraseña.", result);
    }

    @Test
    @DisplayName("❌ Login fallido con usuario inexistente")
    void shouldFailLoginWithNonexistentUser() {
        Usuario loginRequest = new Usuario();
        loginRequest.setUsername("noexiste");
        loginRequest.setPassword("password123");

        when(usuarioRepo.findByUsernameIgnoreCase("noexiste")).thenReturn(Optional.empty());

        String result = usuarioService.login(loginRequest);

        assertEquals("❌ Credenciales inválidas. Verifique usuario o contraseña.", result);
    }

}
