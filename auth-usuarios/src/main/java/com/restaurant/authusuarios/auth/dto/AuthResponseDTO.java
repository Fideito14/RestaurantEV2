package com.restaurant.authusuarios.auth.dto;

import com.restaurant.authusuarios.users.dto.UsuarioResponseDTO;

public record AuthResponseDTO(
        String token,
        UsuarioResponseDTO usuario
) {
}
