package com.ev2.menu_service.controller;

import com.ev2.menu_service.dto.PlatoRequestDTO;
import com.ev2.menu_service.dto.PlatoResponseDTO;
import com.ev2.menu_service.service.PlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;

    private EntityModel<PlatoResponseDTO> toModel(PlatoResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PlatoController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(PlatoController.class).obtenerTodos()).withRel("platos"),
                linkTo(methodOn(PlatoController.class).eliminar(dto.getId())).withRel("eliminar")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<PlatoResponseDTO>> obtenerTodos() {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.obtenerTodos()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping("/disponibles")
    public CollectionModel<EntityModel<PlatoResponseDTO>> obtenerDisponibles() {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.obtenerDisponibles()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).obtenerDisponibles()).withSelfRel());
    }

    @GetMapping("/buscar")
    public CollectionModel<EntityModel<PlatoResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.buscarPorNombre(nombre)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).buscarPorNombre(nombre)).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PlatoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return platoService.obtenerPorId(id)
                .map(dto -> ResponseEntity.ok(toModel(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<PlatoResponseDTO>> crear(@Valid @RequestBody PlatoRequestDTO dto) {
        EntityModel<PlatoResponseDTO> model = toModel(platoService.guardar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PlatoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequestDTO dto) {
        return platoService.actualizar(id, dto)
                .map(updated -> ResponseEntity.ok(toModel(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}