package com.restaurant.authusuarios.userservice.security;

import com.restaurant.authusuarios.userservice.model.Rol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService("12345678901234567890123456789012", 1000L * 60 * 60);

    @Test
    void generateAndValidateToken_shouldWork() {
        String token = jwtService.generarToken("test@example.com", Rol.ADMIN);
        assertNotNull(token);
        assertEquals("test@example.com", jwtService.validar(token).getSubject());
    }
}
