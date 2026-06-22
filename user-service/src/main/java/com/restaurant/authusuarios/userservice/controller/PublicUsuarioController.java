package com.restaurant.authusuarios.userservice.controller;

import com.restaurant.authusuarios.userservice.dto.UsuarioIdDTO;
import com.restaurant.authusuarios.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
public class PublicUsuarioController {

    private final UserService userService;

    @Operation(summary = "Obtener solo el id de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Id encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioIdDTO> obtenerIdPorId(@PathVariable Long id) {
        return userService.obtenerPorId(id)
                .map(u -> ResponseEntity.ok(new UsuarioIdDTO(u.id())))
                .orElse(ResponseEntity.notFound().build());
    }
}
