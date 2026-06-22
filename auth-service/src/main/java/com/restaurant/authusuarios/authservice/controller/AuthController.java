package com.restaurant.authusuarios.authservice.controller;

import com.restaurant.authusuarios.authservice.dto.AuthResponseDTO;
import com.restaurant.authusuarios.authservice.dto.BootstrapAdminRequestDTO;
import com.restaurant.authusuarios.authservice.dto.LoginRequestDTO;
import com.restaurant.authusuarios.authservice.dto.RegisterRequestDTO;
import com.restaurant.authusuarios.authservice.dto.ValidateResponseDTO;
import com.restaurant.authusuarios.authservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.authservice.service.AuthService;
import com.restaurant.authusuarios.authservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(201).body(authService.register(dto));
    }

    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(summary = "Validar token actual")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/validate")
    public ResponseEntity<ValidateResponseDTO> validate(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(new ValidateResponseDTO(true, usuarioService.validateCurrent(email)));
    }

    @Operation(summary = "Crear admin inicial")
    @PostMapping("/bootstrap-admin")
    public ResponseEntity<UsuarioResponseDTO> bootstrapAdmin(@Valid @RequestBody BootstrapAdminRequestDTO dto) {
        return ResponseEntity.status(201).body(authService.bootstrapAdmin(dto));
    }
}
