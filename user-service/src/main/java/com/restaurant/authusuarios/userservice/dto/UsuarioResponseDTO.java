package com.restaurant.authusuarios.userservice.dto;

import com.restaurant.authusuarios.userservice.model.Rol;

public record UsuarioResponseDTO(
        Long id,
        String email,
        Rol rol,
        Boolean activo
) {
}
