package com.restaurant.authusuarios.auth.controller;

import com.restaurant.authusuarios.auth.dto.*;
import com.restaurant.authusuarios.auth.service.AuthService;
import com.restaurant.authusuarios.users.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(201).body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/bootstrap-admin")
    public ResponseEntity<UsuarioResponseDTO> bootstrapAdmin(@Valid @RequestBody BootstrapAdminRequestDTO dto) {
        return ResponseEntity.status(201).body(authService.bootstrapAdmin(dto));
    }
}
