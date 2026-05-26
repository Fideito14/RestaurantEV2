package com.restaurant.authusuarios.users.service;

import com.restaurant.authusuarios.users.dto.CrearUsuarioRequestDTO;
import com.restaurant.authusuarios.users.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.users.model.Usuario;
import com.restaurant.authusuarios.users.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getEmail(), u.getRol(), u.getActivo());
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    public UsuarioResponseDTO crear(CrearUsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        Usuario u = new Usuario(
                null,
                dto.email().toLowerCase(),
                passwordEncoder.encode(dto.password()),
                dto.rol(),
                true,
                LocalDateTime.now()
        );
        return mapToDTO(usuarioRepository.save(u));
    }

    public void desactivar(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        u.setActivo(false);
        usuarioRepository.save(u);
    }

    public UsuarioResponseDTO obtenerActual(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("No autenticado.");
        }
        Usuario u = usuarioRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return mapToDTO(u);
    }
}
