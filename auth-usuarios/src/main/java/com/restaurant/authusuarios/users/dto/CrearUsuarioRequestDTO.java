package com.restaurant.authusuarios.users.dto;

import com.restaurant.authusuarios.users.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequestDTO(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 72) String password,
        @NotNull Rol rol
) {
}
