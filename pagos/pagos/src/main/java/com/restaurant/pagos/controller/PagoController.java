package com.restaurant.pagos.controller;


import com.restaurant.pagos.dto.PagoDTO;
import com.restaurant.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public PagoDTO registrarPago(@Valid @RequestBody PagoDTO pagoDTO){
        return pagoService.registrarPago(pagoDTO);
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<PagoDTO> obtenerPagosPorPedido(@PathVariable Long pedidoId){
        return pagoService.obtenerPagosPorPedido(pedidoId);
    }

    @PutMapping("/{id}/estado")
    public PagoDTO actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return pagoService.actualizarEstado(id, estado);
    }

    @GetMapping("/{id}")
    public PagoDTO obtenerPagoPorId(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id);
    }

    @GetMapping
    public List<PagoDTO> obtenerTodosLosPagos() {
        return pagoService.obtenerTodosLosPagos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }




}
