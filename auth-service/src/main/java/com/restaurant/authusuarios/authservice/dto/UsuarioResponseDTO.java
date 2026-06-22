package com.restaurant.authusuarios.authservice.dto;

import java.util.Set;

public record UsuarioResponseDTO(
        Long id,
        String email,
        String nombre,
        Boolean enabled,
        Set<String> roles
) {
}
