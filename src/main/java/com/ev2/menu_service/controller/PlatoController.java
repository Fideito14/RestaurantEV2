package com.ev2.menu_service.controller;

import com.ev2.menu_service.dto.PlatoRequestDTO;
import com.ev2.menu_service.dto.PlatoResponseDTO;
import com.ev2.menu_service.service.PlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;

    // GET /api/platos
    @GetMapping
    public List<PlatoResponseDTO> obtenerTodos() {
        return platoService.obtenerTodos();
    }

    // GET /api/platos/disponibles
    @GetMapping("/disponibles")
    public List<PlatoResponseDTO> obtenerDisponibles() {
        return platoService.obtenerDisponibles();
    }

    // GET /api/platos/buscar?nombre=pizza
    @GetMapping("/buscar")
    public List<PlatoResponseDTO> buscarPorNombre(@RequestParam String nombre) {
        return platoService.buscarPorNombre(nombre);
    }

    // GET /api/platos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return platoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/platos
    @PostMapping
    public ResponseEntity<PlatoResponseDTO> crear(@Valid @RequestBody PlatoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(platoService.guardar(dto));
    }

    // PUT /api/platos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequestDTO dto) {
        return platoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/platos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}