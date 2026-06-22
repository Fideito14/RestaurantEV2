package com.restaurant.authusuarios.authservice.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService("12345678901234567890123456789012", 1000L * 60 * 60);

    @Test
    void generateAndValidateToken_shouldWork() {
        String token = jwtService.generateToken("test@example.com", List.of("ADMIN"));
        Claims claims = jwtService.validateToken(token);

        assertEquals("test@example.com", claims.getSubject());
        assertEquals(List.of("ADMIN"), claims.get("roles", List.class));
    }
}
