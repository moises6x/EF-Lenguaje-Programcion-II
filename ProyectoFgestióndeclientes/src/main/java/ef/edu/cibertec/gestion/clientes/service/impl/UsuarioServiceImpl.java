package ef.edu.cibertec.gestion.clientes.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ef.edu.cibertec.gestion.clientes.api.request.LoginRequestDto;
import ef.edu.cibertec.gestion.clientes.api.request.UsuarioRequestDto;
import ef.edu.cibertec.gestion.clientes.api.response.UsuarioResponseDto;
import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.entity.Usuario;
import ef.edu.cibertec.gestion.clientes.mapper.UsuarioMapper;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import ef.edu.cibertec.gestion.clientes.repository.UsuarioRepository;
import ef.edu.cibertec.gestion.clientes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;          // ← para rol por defecto (USER)
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;      // ← BCrypt

    // ========= CONSULTAS =========
    @Override @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarUsuarios() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerUsuario(Integer id) {
        Usuario u = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        return mapper.toResponseDto(u);
    }

    @Override @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorNombre(String username) {
        Usuario u = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return mapper.toResponseDto(u);
    }

    // ========= COMANDOS =========
    @Override
    public UsuarioResponseDto crearUsuario(UsuarioRequestDto request) {
        validarUnicidadUsername(request.getUsername());
        String usernameNormalizado = normalizarUsername(request.getUsername());

        Usuario entity = mapper.toEntity(request);
        entity.setUsername(usernameNormalizado);
        entity.setPassword(passwordEncoder.encode(request.getPassword())); // ← ENCRIPTA
        entity.setFechaCreacion(LocalDateTime.now());

        // Rol por defecto: USER (si no vienen roles en el request)
        if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
            Rol rolUser = rolRepository.findByNombreRolIgnoreCase("USER")
                    .orElseThrow(() -> new RuntimeException("Debe existir un rol 'USER'"));
            entity.setRoles(java.util.Set.of(rolUser));
        }

        Usuario saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public UsuarioResponseDto actualizarUsuario(Integer id, UsuarioRequestDto request) {
        Usuario actual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));

        // Si cambia username → validar unicidad
        if (request.getUsername() != null &&
            !request.getUsername().equalsIgnoreCase(actual.getUsername())) {
            validarUnicidadUsername(request.getUsername());
            actual.setUsername(normalizarUsername(request.getUsername()));
        }

        // Si viene nueva password → encriptar
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            actual.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Mapear (parcial) otros campos del request
        mapper.updateEntityFromDto(request, actual);

        Usuario saved = repository.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        repository.deleteById(id);
    }

    // ========= LOGIN “manual” (sin Spring Security de formulario/JWT) =========
    @Override
    @Transactional(readOnly = true)
    public String login(LoginRequestDto loginRequest) {
        log.info("POST /api/usuarios/login");
        return repository.findByUsernameIgnoreCase(normalizarUsername(loginRequest.getUsername()))
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword())) // ← COMPARA HASH
                .map(u -> "✅ Login exitoso. Bienvenido " + u.getUsername() + "!")
                .orElse("❌ Credenciales inválidas. Verifique usuario o contraseña.");
    }

    // ========= Helpers =========
    private void validarUnicidadUsername(String username) {
        if (username == null) return;
        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
    }

    private String normalizarUsername(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}
