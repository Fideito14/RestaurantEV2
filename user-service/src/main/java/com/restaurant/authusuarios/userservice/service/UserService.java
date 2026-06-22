package com.restaurant.authusuarios.userservice.service;

import com.restaurant.authusuarios.userservice.client.AuthServiceClient;
import com.restaurant.authusuarios.userservice.dto.CrearUsuarioRequestDTO;
import com.restaurant.authusuarios.userservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.userservice.model.UserProfile;
import com.restaurant.authusuarios.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final AuthServiceClient authServiceClient;

    private UsuarioResponseDTO mapToDTO(UserProfile u) {
        return new UsuarioResponseDTO(u.getId(), u.getEmail(), u.getRol(), u.getActivo());
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        return userProfileRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return userProfileRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<UsuarioResponseDTO> obtenerPorEmail(String email) {
        return userProfileRepository.findByEmailIgnoreCase(email).map(this::mapToDTO);
    }

    public UsuarioResponseDTO crear(CrearUsuarioRequestDTO dto) {
        if (userProfileRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        UserProfile user = new UserProfile(
                null,
                dto.email().toLowerCase(),
                dto.rol(),
                true,
                LocalDateTime.now()
        );
        return mapToDTO(userProfileRepository.save(user));
    }

    public void desactivar(Long id) {
        UserProfile user = userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        user.setActivo(false);
        userProfileRepository.save(user);
        authServiceClient.deactivateAccountByEmail(user.getEmail());
    }

    public UsuarioResponseDTO obtenerActual(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("No autenticado.");
        }
        UserProfile user = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return mapToDTO(user);
    }
}
