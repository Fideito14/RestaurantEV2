package com.restaurant.authusuarios.authservice.service;

import com.restaurant.authusuarios.authservice.dto.AuthResponseDTO;
import com.restaurant.authusuarios.authservice.dto.LoginRequestDTO;
import com.restaurant.authusuarios.authservice.dto.RegisterRequestDTO;
import com.restaurant.authusuarios.authservice.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.authservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final UsuarioService usuarioService = mock(UsuarioService.class);
    private final JwtService jwtService = new JwtService("12345678901234567890123456789012", 1000L * 60 * 60);
    private final AuthService authService = new AuthService(authenticationManager, usuarioService, jwtService);

    @Test
    void register_shouldGenerateToken() {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO(1L, "a@b.com", "Ana", true, Set.of("CLIENTE"));
        when(usuarioService.registrar(any(RegisterRequestDTO.class), anyString())).thenReturn(usuario);

        AuthResponseDTO response = authService.register(new RegisterRequestDTO("a@b.com", "secret12", "Ana"));

        assertEquals("a@b.com", response.usuario().email());
        assertEquals("Ana", response.usuario().nombre());
        assertFalse(response.token().isBlank());
    }

    @Test
    void login_withValidCredentials_shouldGenerateToken() {
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(usuarioService.validateCurrent("a@b.com"))
                .thenReturn(new UsuarioResponseDTO(1L, "a@b.com", "Ana", true, Set.of("CLIENTE")));

        AuthResponseDTO response = authService.login(new LoginRequestDTO("a@b.com", "secret12"));

        assertEquals("a@b.com", response.usuario().email());
        assertFalse(response.token().isBlank());
    }

    @Test
    void login_withInvalidPassword_shouldFail() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequestDTO("a@b.com", "wrong12")));
    }
}
