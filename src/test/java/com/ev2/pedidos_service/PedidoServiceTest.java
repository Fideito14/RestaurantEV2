package com.ev2.pedidos_service;

import com.ev2.pedidos_service.dto.PedidoRequestDTO;
import com.ev2.pedidos_service.dto.PedidoResponseDTO;
import com.ev2.pedidos_service.model.Pedido;
import com.ev2.pedidos_service.repository.PedidoRepository;
import com.ev2.pedidos_service.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private PedidoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        pedido = new Pedido(1L, 1L, 2, 3, "PENDIENTE");
        requestDTO = new PedidoRequestDTO(1L, 2, 3, "PENDIENTE");
    }

    @Test
    void obtenerTodos_debeRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<PedidoResponseDTO> resultado = pedidoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(3, resultado.get().getMesa());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PedidoResponseDTO> resultado = pedidoService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void obtenerPorEstado_debeRetornarPedidosFiltrados() {
        when(pedidoRepository.findByEstado("PENDIENTE")).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerPorEstado("PENDIENTE");

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
    }

    @Test
    void obtenerPorMesa_debeRetornarPedidosFiltrados() {
        when(pedidoRepository.findByMesa(3)).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerPorMesa(3);

        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getMesa());
    }

    @Test
    void obtenerPorPlatoId_debeRetornarPedidosFiltrados() {
        when(pedidoRepository.findByPlatoId(1L)).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> resultado = pedidoService.obtenerPorPlatoId(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getPlatoId());
    }

    @Test
    void guardar_cuandoPlatoExiste_debeCrearPedido() {
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponseDTO resultado = pedidoService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals(3, resultado.getMesa());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void eliminar_debeInvocarDeleteById() {
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.eliminar(1L);

        verify(pedidoRepository, times(1)).deleteById(1L);
    }
}