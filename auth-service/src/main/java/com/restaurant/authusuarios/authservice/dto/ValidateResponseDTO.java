package com.restaurant.authusuarios.authservice.dto;

public record ValidateResponseDTO(
        boolean valid,
        UsuarioResponseDTO usuario
) {
}
