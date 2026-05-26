package com.restaurant.mesa.controller;

import com.restaurant.mesa.dto.MesaDTO;
import com.restaurant.mesa.Service.MesaService;
import com.restaurant.mesa.model.Mesa;
import com.restaurant.mesa.repository.MesaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;
    private final MesaRepository mesaRepository;

    public MesaController(MesaService mesaService, MesaRepository mesaRepository) {
        this.mesaService = mesaService;
        this.mesaRepository = mesaRepository;
    }

    // Crear una nueva mesa
    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody MesaDTO dto) {
        Mesa nuevaMesa = mesaService.crearMesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    @GetMapping
    public ResponseEntity<List<MesaDTO>> listarMesas() {
        List<MesaDTO> mesas = mesaService.listarMesas().stream()
                .map(mesa -> {
                    MesaDTO dto = new MesaDTO();
                    dto.setId(mesa.getId());
                    dto.setNumero(mesa.getNumero());
                    dto.setEstado(mesa.getEstado());
                    dto.setUsuarioId(mesa.getUsuarioId());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(mesas);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Mesa> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) Long usuarioId) {
        Mesa mesa = mesaService.cambiarEstado(id, estado, usuarioId);
        return ResponseEntity.ok(mesa);
    }



}
