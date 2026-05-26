package com.restaurant.authusuarios.users.controller;

import com.restaurant.authusuarios.users.dto.UsuarioIdDTO;
import com.restaurant.authusuarios.users.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
public class PublicUsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioIdDTO> obtenerIdPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(u -> ResponseEntity.ok(new UsuarioIdDTO(u.id())))
                .orElse(ResponseEntity.notFound().build());
    }
}

