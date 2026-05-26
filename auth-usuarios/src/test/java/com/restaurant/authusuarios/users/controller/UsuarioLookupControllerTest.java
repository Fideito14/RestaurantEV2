package com.restaurant.authusuarios.users.controller;

import com.restaurant.authusuarios.auth.security.JwtService;
import com.restaurant.authusuarios.users.dto.UsuarioResponseDTO;
import com.restaurant.authusuarios.users.model.Rol;
import com.restaurant.authusuarios.users.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioLookupController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioLookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void getById_whenExists_returnsIdOnly() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(
                new UsuarioResponseDTO(1L, "a@b.com", Rol.CLIENTE, true)
        ));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1}"));
    }

    @Test
    void getById_whenMissing_returns404() throws Exception {
        when(usuarioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/999"))
                .andExpect(status().isNotFound());
    }
}

