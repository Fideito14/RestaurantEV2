package com.ev2.menu_service.controller;

import com.ev2.menu_service.dto.PlatoRequestDTO;
import com.ev2.menu_service.dto.PlatoResponseDTO;
import com.ev2.menu_service.service.PlatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Platos", description = "Gestión del menú del restaurante")
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

    @Operation(summary = "Listar todos los platos", description = "Retorna todos los platos registrados en el menú, disponibles o no.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de platos obtenida correctamente",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "[{\"id\":1,\"nombre\":\"Pizza Margherita\",\"descripcion\":\"Pizza con tomate y mozzarella\",\"precio\":8990.0,\"disponible\":true}]")))
    })
    @GetMapping
    public CollectionModel<EntityModel<PlatoResponseDTO>> obtenerTodos() {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.obtenerTodos()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).obtenerTodos()).withSelfRel());
    }

    @Operation(summary = "Listar platos disponibles", description = "Retorna únicamente los platos marcados como disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de platos disponibles obtenida correctamente")
    @GetMapping("/disponibles")
    public CollectionModel<EntityModel<PlatoResponseDTO>> obtenerDisponibles() {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.obtenerDisponibles()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).obtenerDisponibles()).withSelfRel());
    }

    @Operation(summary = "Buscar platos por nombre", description = "Búsqueda parcial e insensible a mayúsculas por el nombre del plato.")
    @ApiResponse(responseCode = "200", description = "Lista de platos que coinciden con el nombre buscado")
    @GetMapping("/buscar")
    public CollectionModel<EntityModel<PlatoResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar dentro del nombre del plato", example = "pizza")
            @RequestParam String nombre) {
        List<EntityModel<PlatoResponseDTO>> platos = platoService.buscarPorNombre(nombre)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(platos,
                linkTo(methodOn(PlatoController.class).buscarPorNombre(nombre)).withSelfRel());
    }

    @Operation(summary = "Obtener plato por ID", description = "Retorna un plato específico según su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un plato con el ID proporcionado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PlatoResponseDTO>> obtenerPorId(
            @Parameter(description = "ID del plato a buscar", example = "1")
            @PathVariable Long id) {
        return platoService.obtenerPorId(id)
                .map(dto -> ResponseEntity.ok(toModel(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo plato", description = "Registra un nuevo plato en el menú con sus validaciones correspondientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<PlatoResponseDTO>> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del plato a crear",
                    content = @Content(examples = @ExampleObject(value = "{\"nombre\":\"Pizza Margherita\",\"descripcion\":\"Pizza con tomate y mozzarella\",\"precio\":8990.0,\"disponible\":true}")))
            @Valid @RequestBody PlatoRequestDTO dto) {
        EntityModel<PlatoResponseDTO> model = toModel(platoService.guardar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Actualizar un plato existente", description = "Modifica los datos de un plato según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un plato con el ID proporcionado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PlatoResponseDTO>> actualizar(
            @Parameter(description = "ID del plato a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PlatoRequestDTO dto) {
        return platoService.actualizar(id, dto)
                .map(updated -> ResponseEntity.ok(toModel(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un plato", description = "Elimina un plato del menú según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plato eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe un plato con el ID proporcionado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del plato a eliminar", example = "1")
            @PathVariable Long id) {
        platoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}