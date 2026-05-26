package com.restaurant.authusuarios.auth.service;

import com.restaurant.authusuarios.auth.dto.*;
import com.restaurant.authusuarios.auth.security.JwtService;
import com.restaurant.authusuarios.users.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.users.model.Rol;
import com.restaurant.authusuarios.users.model.Usuario;
import com.restaurant.authusuarios.users.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getEmail(), u.getRol(), u.getActivo());
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        Usuario u = new Usuario(
                null,
                dto.email().toLowerCase(),
                passwordEncoder.encode(dto.password()),
                Rol.CLIENTE,
                true,
                LocalDateTime.now()
        );
        u = usuarioRepository.save(u);
        String token = jwtService.generarToken(u.getEmail(), u.getRol());
        return new AuthResponseDTO(token, mapToDTO(u));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        Usuario u = usuarioRepository.findByEmail(dto.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!Boolean.TRUE.equals(u.getActivo())) {
            throw new IllegalArgumentException("Usuario desactivado.");
        }
        if (!passwordEncoder.matches(dto.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        String token = jwtService.generarToken(u.getEmail(), u.getRol());
        return new AuthResponseDTO(token, mapToDTO(u));
    }

    /**
     * Endpoint de arranque para crear el primer ADMIN cuando aún no existe ninguno.
     * Útil cuando trabajas en proyectos separados y todavía no está definido el seed en Flyway.
     */
    public UsuarioResponseDTO bootstrapAdmin(BootstrapAdminRequestDTO dto) {
        if (usuarioRepository.existsByRol(Rol.ADMIN)) {
            throw new IllegalArgumentException("Ya existe un ADMIN. Bootstrap deshabilitado.");
        }
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Usuario u = new Usuario(
                null,
                dto.email().toLowerCase(),
                passwordEncoder.encode(dto.password()),
                Rol.ADMIN,
                true,
                LocalDateTime.now()
        );
        return mapToDTO(usuarioRepository.save(u));
    }
}
