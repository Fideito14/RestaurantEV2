package com.ev2.pedidos_service.service;

import com.ev2.pedidos_service.dto.PedidoRequestDTO;
import com.ev2.pedidos_service.dto.PedidoResponseDTO;
import com.ev2.pedidos_service.model.Pedido;
import com.ev2.pedidos_service.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private PedidoResponseDTO mapToDTO(Pedido p) {
        return new PedidoResponseDTO(
                p.getId(),
                p.getPlatoId(),
                p.getCantidad(),
                p.getMesa(),
                p.getEstado()
        );
    }

    public List<PedidoResponseDTO> obtenerTodos() {
        log.info(">>> Obteniendo todos los pedidos");
        return pedidoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PedidoResponseDTO> obtenerPorId(Long id) {
        log.info(">>> Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id).map(this::mapToDTO);
    }

    public List<PedidoResponseDTO> obtenerPorEstado(String estado) {
        log.info(">>> Obteniendo pedidos con estado: {}", estado);
        return pedidoRepository.findByEstado(estado)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> obtenerPorMesa(Integer mesa) {
        log.info(">>> Obteniendo pedidos de la mesa: {}", mesa);
        return pedidoRepository.findByMesa(mesa)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoResponseDTO> obtenerPorPlatoId(Long platoId) {
        log.info(">>> Obteniendo pedidos del plato con id: {}", platoId);
        return pedidoRepository.findByPlatoId(platoId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO guardar(PedidoRequestDTO dto) {
        log.info(">>> Creando pedido para mesa: {}", dto.getMesa());
        Pedido pedido = new Pedido(
                null,
                dto.getPlatoId(),
                dto.getCantidad(),
                dto.getMesa(),
                dto.getEstado()
        );
        return mapToDTO(pedidoRepository.save(pedido));
    }

    public Optional<PedidoResponseDTO> actualizar(Long id, PedidoRequestDTO dto) {
        log.info(">>> Actualizando pedido con id: {}", id);
        return pedidoRepository.findById(id).map(existente -> {
            existente.setPlatoId(dto.getPlatoId());
            existente.setCantidad(dto.getCantidad());
            existente.setMesa(dto.getMesa());
            existente.setEstado(dto.getEstado());
            return mapToDTO(pedidoRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        log.info(">>> Eliminando pedido con id: {}", id);
        pedidoRepository.deleteById(id);
    }
}