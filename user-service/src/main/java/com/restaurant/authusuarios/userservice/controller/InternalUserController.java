package com.restaurant.authusuarios.userservice.controller;

import com.restaurant.authusuarios.userservice.dto.CrearUsuarioRequestDTO;
import com.restaurant.authusuarios.userservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @Operation(summary = "Crear usuario interno")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody CrearUsuarioRequestDTO dto) {
        return ResponseEntity.status(201).body(userService.crear(dto));
    }

    @Operation(summary = "Buscar usuario interno por email")
    @GetMapping("/by-email/{email}")
    public ResponseEntity<UsuarioResponseDTO> byEmail(@PathVariable String email) {
        return userService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
