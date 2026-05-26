package com.restaurant.authusuarios.users.dto;

import com.restaurant.authusuarios.users.model.Rol;

public record UsuarioResponseDTO(
        Long id,
        String email,
        Rol rol,
        Boolean activo
) {
}
