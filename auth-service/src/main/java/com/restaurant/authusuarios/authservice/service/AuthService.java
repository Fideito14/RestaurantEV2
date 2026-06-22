package com.restaurant.authusuarios.authservice.service;

import com.restaurant.authusuarios.authservice.dto.AuthResponseDTO;
import com.restaurant.authusuarios.authservice.dto.BootstrapAdminRequestDTO;
import com.restaurant.authusuarios.authservice.dto.LoginRequestDTO;
import com.restaurant.authusuarios.authservice.dto.RegisterRequestDTO;
import com.restaurant.authusuarios.authservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.registrar(dto, "CLIENTE");
        String token = jwtService.generateToken(usuario.email(), List.of("CLIENTE"));
        return new AuthResponseDTO(token, usuario);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        UsuarioResponseDTO usuario = usuarioService.validateCurrent(dto.email());
        String token = jwtService.generateToken(usuario.email(), usuario.roles().stream().toList());
        return new AuthResponseDTO(token, usuario);
    }

    public UsuarioResponseDTO bootstrapAdmin(BootstrapAdminRequestDTO dto) {
        boolean adminExists = usuarioService.findAll().stream()
                .anyMatch(usuario -> usuario.roles().contains("ADMIN"));
        if (adminExists) {
            throw new IllegalArgumentException("Ya existe un ADMIN. Bootstrap deshabilitado.");
        }
        return usuarioService.registrarAdmin(dto, "ADMIN");
    }

    public void deactivateByEmail(String email) {
        usuarioService.deactivateByEmail(email);
    }
}
