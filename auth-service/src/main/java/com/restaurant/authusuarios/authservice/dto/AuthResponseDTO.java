package com.restaurant.authusuarios.authservice.dto;

public record AuthResponseDTO(
        String token,
        UsuarioResponseDTO usuario
) {
}
