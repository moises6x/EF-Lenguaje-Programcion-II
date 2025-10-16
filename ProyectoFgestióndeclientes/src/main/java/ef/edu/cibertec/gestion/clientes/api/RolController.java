package ef.edu.cibertec.gestion.clientes.api;



import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ef.edu.cibertec.gestion.clientes.entity.Rol;
import ef.edu.cibertec.gestion.clientes.repository.RolRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    // 🟢 Listar todos los roles
    @GetMapping
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    // 🔵 Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable Integer id) {
        Optional<Rol> rol = rolRepository.findById(id);
        return rol.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🟡 Crear un nuevo rol
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = rolRepository.save(rol);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    // 🟠 Actualizar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Integer id, @RequestBody Rol rol) {
        return rolRepository.findById(id)
                .map(existente -> {
                    existente.setNombreRol(rol.getNombreRol());
                    existente.setDescripcion(rol.getDescripcion());
                    Rol actualizado = rolRepository.save(existente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔴 Eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        if (!rolRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rolRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 🟣 Buscar por nombre de rol
    @GetMapping("/buscar")
    public ResponseEntity<Rol> buscarPorNombre(@RequestParam String nombre) {
        Optional<Rol> rol = rolRepository.findByNombreRolIgnoreCase(nombre);
        return rol.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

