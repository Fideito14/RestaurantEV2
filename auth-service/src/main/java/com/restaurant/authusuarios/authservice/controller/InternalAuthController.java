package com.restaurant.authusuarios.authservice.controller;

import com.restaurant.authusuarios.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class InternalAuthController {

    private final AuthService authService;

    @Operation(summary = "Desactivar usuario por email")
    @PostMapping("/deactivate/{email}")
    public ResponseEntity<Void> deactivate(@PathVariable String email) {
        authService.deactivateByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
