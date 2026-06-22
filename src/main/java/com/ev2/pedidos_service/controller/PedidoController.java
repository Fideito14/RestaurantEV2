package com.ev2.pedidos_service.controller;

import com.ev2.pedidos_service.dto.PedidoRequestDTO;
import com.ev2.pedidos_service.dto.PedidoResponseDTO;
import com.ev2.pedidos_service.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@Tag(name = "Pedidos", description = "Gestión de pedidos del restaurante")
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

    @Operation(summary = "Listar todos los pedidos", description = "Retorna todos los pedidos registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida correctamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(value = "[{\"id\":1,\"platoId\":1,\"cantidad\":2,\"mesa\":3,\"estado\":\"PENDIENTE\"}]")))
    @GetMapping
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerTodos() {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerTodos()
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerTodos()).withSelfRel());
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico según su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un pedido con el ID proporcionado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> obtenerPorId(
            @Parameter(description = "ID del pedido a buscar", example = "1")
            @PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(dto -> ResponseEntity.ok(toModel(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filtrar pedidos por estado", description = "Retorna los pedidos que coincidan con el estado indicado (PENDIENTE, EN_PROCESO, ENTREGADO).")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados por estado")
    @GetMapping("/estado")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorEstado(
            @Parameter(description = "Estado del pedido a filtrar", example = "PENDIENTE")
            @RequestParam String estado) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorEstado(estado)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorEstado(estado)).withSelfRel());
    }

    @Operation(summary = "Filtrar pedidos por mesa", description = "Retorna todos los pedidos asociados a un número de mesa.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados por mesa")
    @GetMapping("/mesa")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorMesa(
            @Parameter(description = "Número de mesa a filtrar", example = "3")
            @RequestParam Integer mesa) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorMesa(mesa)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorMesa(mesa)).withSelfRel());
    }

    @Operation(summary = "Filtrar pedidos por plato", description = "Retorna todos los pedidos asociados a un plato específico del menu-service.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados por plato")
    @GetMapping("/plato/{platoId}")
    public CollectionModel<EntityModel<PedidoResponseDTO>> obtenerPorPlatoId(
            @Parameter(description = "ID del plato a filtrar", example = "1")
            @PathVariable Long platoId) {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.obtenerPorPlatoId(platoId)
                .stream().map(this::toModel).collect(Collectors.toList());
        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPorPlatoId(platoId)).withSelfRel());
    }

    @Operation(summary = "Crear un nuevo pedido", description = "Registra un pedido validando previamente que el platoId exista en menu-service mediante RestTemplate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
            @ApiResponse(responseCode = "400", description = "El plato indicado no existe en menu-service o los datos son inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<PedidoResponseDTO>> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del pedido a crear",
                    content = @Content(examples = @ExampleObject(value = "{\"platoId\":1,\"cantidad\":2,\"mesa\":3,\"estado\":\"PENDIENTE\"}")))
            @Valid @RequestBody PedidoRequestDTO dto) {
        EntityModel<PedidoResponseDTO> model = toModel(pedidoService.guardar(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Actualizar un pedido existente", description = "Modifica los datos de un pedido, validando nuevamente que el platoId exista en menu-service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "El plato indicado no existe en menu-service", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe un pedido con el ID proporcionado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> actualizar(
            @Parameter(description = "ID del pedido a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return pedidoService.actualizar(id, dto)
                .map(updated -> ResponseEntity.ok(toModel(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido del sistema según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe un pedido con el ID proporcionado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pedido a eliminar", example = "1")
            @PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}