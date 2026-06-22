package com.restaurant.authusuarios.authservice.service;

import com.restaurant.authusuarios.authservice.dto.RegisterRequestDTO;
import com.restaurant.authusuarios.authservice.model.Rol;
import com.restaurant.authusuarios.authservice.repository.RolRepository;
import com.restaurant.authusuarios.authservice.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final RolRepository rolRepository = mock(RolRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UsuarioService usuarioService = new UsuarioService(usuarioRepository, rolRepository, passwordEncoder);

    @Test
    void registrar_whenEmailExists_shouldFail() {
        when(usuarioRepository.existsByEmailIgnoreCase("a@b.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrar(new RegisterRequestDTO("a@b.com", "secret12", "Ana"), "CLIENTE"));
    }

    @Test
    void validateCurrent_shouldReturnDto() {
        var rol = new Rol(1L, "CLIENTE");
        var usuario = new com.restaurant.authusuarios.authservice.model.Usuario(
                1L, "a@b.com", "hash", "Ana", true, Set.of(rol), java.time.LocalDateTime.now()
        );
        when(usuarioRepository.findByEmailIgnoreCase("a@b.com")).thenReturn(Optional.of(usuario));

        assertEquals("Ana", usuarioService.validateCurrent("a@b.com").nombre());
    }

    @Test
    void validateCurrent_whenMissing_shouldFail() {
        when(usuarioRepository.findByEmailIgnoreCase("nope@b.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usuarioService.validateCurrent("nope@b.com"));
    }
}
