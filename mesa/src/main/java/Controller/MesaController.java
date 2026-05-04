package Controller;

import Model.Mesa;
import Repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesa")
@RequiredArgsConstructor
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas() {
        try {
            List<Mesa> mesas = mesaRepository.findAll();
            return ResponseEntity.ok(mesas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa mesa) {
        try {
            Mesa nuevaMesa = mesaRepository.save(mesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerMesa(@PathVariable Integer id) {
        try {
            return mesaRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizarMesa(@PathVariable Integer id, @RequestBody Mesa mesaActualizada) {
        try {
            if (!mesaRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            mesaActualizada.setIdMesa(id);
            Mesa mesaGuardada = mesaRepository.save(mesaActualizada);
            return ResponseEntity.ok(mesaGuardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMesa(@PathVariable Integer id) {
        try {
            if (!mesaRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            mesaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/reservar/{id}")
    public ResponseEntity<Mesa> reservarMesa(@PathVariable Integer id, @RequestParam Long usuarioId) {
        try {
            Optional<Mesa> optionalMesa = mesaRepository.findById(id);

            if (optionalMesa.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404 si no existe
            }

            Mesa mesa = optionalMesa.get();

            if ("Reservado".equalsIgnoreCase(mesa.getEstado())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 si ya está reservada
            }

            mesa.setEstado("Reservado");
            mesa.setUsuarioId(usuarioId);
            Mesa mesaGuardada = mesaRepository.save(mesa);

            return ResponseEntity.ok(mesaGuardada); // 200 OK con la mesa reservada
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 si algo falla
        }
    }

    @PutMapping("/liberar/{id}")
    public ResponseEntity<Mesa> liberarMesa(@PathVariable Integer id) {
        try {
            Optional<Mesa> optionalMesa = mesaRepository.findById(id);

            if (optionalMesa.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404 si no existe
            }

            Mesa mesa = optionalMesa.get();

            if ("Libre".equalsIgnoreCase(mesa.getEstado())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 si ya está libre
            }

            mesa.setEstado("Libre");
            mesa.setUsuarioId(null);
            Mesa mesaGuardada = mesaRepository.save(mesa);

            return ResponseEntity.ok(mesaGuardada); // 200 OK con la mesa liberada
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/libres")
    public ResponseEntity<List<Mesa>> listarMesasLibres() {
        try {
            List<Mesa> libres = mesaRepository.findByEstado("Libre");
            return ResponseEntity.ok(libres);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
