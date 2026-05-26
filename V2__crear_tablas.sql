package com.restaurant.mesa.controller;

import com.restaurant.mesa.dto.ReservaDTO;
import com.restaurant.mesa.Service.ReservaService;
import com.restaurant.mesa.model.Reserva;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    //crear reserva
    @PutMapping("/reservas")
    public ResponseEntity<Reserva> crearReserva(
            @RequestParam Long mesaId,
            @RequestParam Long usuarioId) {

        Reserva reserva = reservaService.crearReserva(mesaId, usuarioId);
        return ResponseEntity.ok(reserva);
    }


    // Cancelar reserva
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(@PathVariable Long id) {
        return reservaService.cancelarReserva(id)
                .map(reserva -> new ReservaDTO(
                        reserva.getId(),
                        reserva.getMesaId().intValue(),
                        reserva.getUsuarioId(),
                        reserva.getFecha(),
                        reserva.getEstado()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Finalizar reserva
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ReservaDTO> finalizarReserva(@PathVariable Long id) {
        return reservaService.finalizarReserva(id)
                .map(reserva -> new ReservaDTO(
                        reserva.getId(),
                        reserva.getMesaId().intValue(),
                        reserva.getUsuarioId(),
                        reserva.getFecha(),
                        reserva.getEstado()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reservas = reservaService.listarReservas().stream()
                .map(reserva -> new ReservaDTO(
                        reserva.getId(),
                        reserva.getMesaId().intValue(),
                        reserva.getUsuarioId(),
                        reserva.getFecha(),
                        reserva.getEstado()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservas);
    }

    // Listar reservas por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<ReservaDTO> reservas = reservaService.listarReservasPorUsuario(usuarioId).stream()
                .map(reserva -> new ReservaDTO(
                        reserva.getId(),
                        reserva.getMesaId().intValue(),
                        reserva.getUsuarioId(),
                        reserva.getFecha(),
                        reserva.getEstado()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservas);
    }

    // Listar reservas por mesa
    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<List<ReservaDTO>> listarPorMesa(@PathVariable Long mesaId) {
        List<ReservaDTO> reservas = reservaService.listarReservasPorMesa(mesaId).stream()
                .map(reserva -> new ReservaDTO(
                        reserva.getId(),
                        reserva.getMesaId().intValue(),
                        reserva.getUsuarioId(),
                        reserva.getFecha(),
                        reserva.getEstado()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservas);
    }
}
