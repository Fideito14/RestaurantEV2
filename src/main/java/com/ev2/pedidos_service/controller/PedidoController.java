package com.ev2.pedidos_service.controller;

import com.ev2.pedidos_service.dto.PedidoRequestDTO;
import com.ev2.pedidos_service.dto.PedidoResponseDTO;
import com.ev2.pedidos_service.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    private EntityModel<PedidoResponseDTO> toModel(PedidoResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PedidoController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos"),
                linkTo(methodOn(PedidoController.class).obtenerPorMesa(dto.getMesa())).withRel("pedidos-mesa"),
                linkTo(methodOn(PedidoController.class).eliminar(dto.getId())).withRel("eliminar")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerTodos() {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerTodos()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(dto -> ResponseEntity.ok(toModel(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorEstado(@RequestParam String estado) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorEstado(estado)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorEstado(estado)).withSelfRel());
    }

    @GetMapping("/mesa")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorMesa(@RequestParam Integer mesa) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorMesa(mesa)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorMesa(mesa)).withSelfRel());
    }

    @GetMapping("/plato/{platoId}")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorPlatoId(@PathVariable Long platoId) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorPlatoId(platoId)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorPlatoId(platoId)).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<EntityModel<PedidoResponseDTO>> crear(@Valid @RequestBody PedidoRequestDTO dto) {
        EntityModel<PedidoResponseDTO> model = toModel(pedidoService.guardar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return pedidoService.actualizar(id, dto)
                .map(updated -> ResponseEntity.ok(toModel(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}