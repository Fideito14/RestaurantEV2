package com.ev2.pedidos_service.controller;

import com.ev2.pedidos_service.dto.PedidoRequestDTO;
import com.ev2.pedidos_service.dto.PedidoResponseDTO;
import com.ev2.pedidos_service.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // GET /api/pedidos
    @GetMapping
    public List<PedidoResponseDTO> obtenerTodos() {
        return pedidoService.obtenerTodos();
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pedidos/estado?estado=PENDIENTE
    @GetMapping("/estado")
    public List<PedidoResponseDTO> obtenerPorEstado(@RequestParam String estado) {
        return pedidoService.obtenerPorEstado(estado);
    }

    // GET /api/pedidos/mesa?mesa=5
    @GetMapping("/mesa")
    public List<PedidoResponseDTO> obtenerPorMesa(@RequestParam Integer mesa) {
        return pedidoService.obtenerPorMesa(mesa);
    }

    // GET /api/pedidos/plato/{platoId}
    @GetMapping("/plato/{platoId}")
    public List<PedidoResponseDTO> obtenerPorPlatoId(@PathVariable Long platoId) {
        return pedidoService.obtenerPorPlatoId(platoId);
    }

    // POST /api/pedidos
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crear(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.guardar(dto));
    }

    // PUT /api/pedidos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return pedidoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/pedidos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}