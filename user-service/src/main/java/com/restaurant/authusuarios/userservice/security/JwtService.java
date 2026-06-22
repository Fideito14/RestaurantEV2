package com.restaurant.authusuarios.userservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.restaurant.authusuarios.userservice.model.Rol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationMinutes = expirationMinutes;
    }

    public String generarToken(String email, Rol rol) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(email)
                .withClaim("role", rol.name())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public DecodedJWT validar(String token) throws JWTVerificationException {
        return verifier.verify(token);
    }
}
