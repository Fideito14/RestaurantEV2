package com.restaurant.authusuarios.authservice.service;

import com.restaurant.authusuarios.authservice.dto.BootstrapAdminRequestDTO;
import com.restaurant.authusuarios.authservice.dto.RegisterRequestDTO;
import com.restaurant.authusuarios.authservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.authservice.model.Rol;
import com.restaurant.authusuarios.authservice.model.Usuario;
import com.restaurant.authusuarios.authservice.repository.RolRepository;
import com.restaurant.authusuarios.authservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO registrar(RegisterRequestDTO dto, String rolPorDefecto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        Rol rol = rolRepository.findByNombre(rolPorDefecto)
                .orElseThrow(() -> new IllegalStateException("Rol por defecto no existe."));

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setNombre(dto.nombre());
        usuario.setEnabled(true);
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setRoles(new HashSet<>(Set.of(rol)));

        return toDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO registrarAdmin(BootstrapAdminRequestDTO dto, String adminRole) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        Rol rol = rolRepository.findByNombre(adminRole)
                .orElseThrow(() -> new IllegalStateException("Rol ADMIN no existe."));

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setNombre(dto.nombre());
        usuario.setEnabled(true);
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setRoles(new HashSet<>(Set.of(rol)));

        return toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> findById(Long id) {
        return usuarioRepository.findById(id).map(this::toDTO);
    }

    public Optional<Usuario> findEntityByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email);
    }

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getEnabled(),
                usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet())
        );
    }

    public UsuarioResponseDTO validateCurrent(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return toDTO(usuario);
    }

    public void deactivateByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
    }
}
