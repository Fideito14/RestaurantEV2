package com.restaurant.authusuarios.userservice.dto;

import com.restaurant.authusuarios.userservice.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearUsuarioRequestDTO(
        @NotBlank @Email String email,
        @NotNull Rol rol
) {
}
