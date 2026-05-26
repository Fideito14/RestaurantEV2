package com.restaurant.mesa.client;

import com.restaurant.mesa.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service",url = "${ms.usuarios.url}")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioDTO getUsuarioById(@PathVariable Long id);
}
