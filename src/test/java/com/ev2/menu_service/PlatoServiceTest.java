package com.ev2.menu_service;

import com.ev2.menu_service.dto.PlatoRequestDTO;
import com.ev2.menu_service.dto.PlatoResponseDTO;
import com.ev2.menu_service.model.Plato;
import com.ev2.menu_service.repository.PlatoRepository;
import com.ev2.menu_service.service.PlatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceTest {

    @Mock
    private PlatoRepository platoRepository;

    @InjectMocks
    private PlatoService platoService;

    private Plato plato;
    private PlatoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        plato = new Plato(1L, "Pizza Margherita", "Descripcion", 8990.0, true);
        requestDTO = new PlatoRequestDTO("Pizza Margherita", "Descripcion", 8990.0, true);
    }

    @Test
    void obtenerTodos_debeRetornarListaDePlatos() {
        when(platoRepository.findAll()).thenReturn(List.of(plato));
        List<PlatoResponseDTO> resultado = platoService.obtenerTodos();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarPlato() {
        when(platoRepository.findById(1L)).thenReturn(Optional.of(plato));
        Optional<PlatoResponseDTO> resultado = platoService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Pizza Margherita", resultado.get().getNombre());
    }

    @Test
    void guardar_debeCrearYRetornarPlato() {
        when(platoRepository.save(any(Plato.class))).thenReturn(plato);
        PlatoResponseDTO resultado = platoService.guardar(requestDTO);
        assertNotNull(resultado);
        assertEquals(8990.0, resultado.getPrecio());
        verify(platoRepository, times(1)).save(any(Plato.class));
    }

    @Test
    void eliminar_debeInvocarDeleteById() {
        doNothing().when(platoRepository).deleteById(1L);
        platoService.eliminar(1L);
        verify(platoRepository, times(1)).deleteById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(platoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<PlatoResponseDTO> resultado = platoService.obtenerPorId(99L);
        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizar_cuandoNoExiste_debeRetornarVacio() {
        when(platoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<PlatoResponseDTO> resultado = platoService.actualizar(99L, requestDTO);
        assertFalse(resultado.isPresent());
        verify(platoRepository, never()).save(any(Plato.class));
    }
}