package com.restaurant.pagos.service;

import com.restaurant.pagos.client.PedidoClient;
import com.restaurant.pagos.dto.PagoDTO;
import com.restaurant.pagos.dto.PedidoDTO;
import com.restaurant.pagos.model.Pago;
import com.restaurant.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoClient pedidoClient;


    //Metodos publicos

    public PagoDTO registrarPago(PagoDTO pagoDTO) {
        try {
            PedidoDTO pedido = pedidoClient.getPedidoById(pagoDTO.getPedidoId());
            if (pedido == null) {
                log.error("Pedido no encontrado con id: {}", pagoDTO.getPedidoId());
                throw new RuntimeException("El pedido no existe, no se puede registrar el pago");
            }
            if ("CANCELADO".equalsIgnoreCase(pedido.getEstado())) {
                log.error("Pedido con id {} está cancelado", pagoDTO.getPedidoId());
                throw new RuntimeException("El pedido está cancelado, no se puede registrar el pago");
            }
        } catch (Exception e) {
            log.error("Error al validar pedido con id: {}", pagoDTO.getPedidoId(), e);
            throw new RuntimeException("El pedido no existe, no se puede registrar el pago");
        }

        Pago pago = new Pago(
                null,
                pagoDTO.getMonto(),
                LocalDateTime.now(),
                pagoDTO.getMetodo(),
                pagoDTO.getPedidoId(),
                "pendiente"
        );

        Pago guardado = pagoRepository.save(pago);
        return convertirADTO(guardado);
    }

    public List<PagoDTO> obtenerPagosPorPedido(Long pedidoId){
        return pagoRepository.findByPedidoId(pedidoId).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public PagoDTO actualizarEstado(Long id, String nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));

        pago.setEstado(nuevoEstado);
        Pago actualizado = pagoRepository.save(pago);

        return convertirADTO(actualizado);
    }

    public PagoDTO obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        return convertirADTO(pago);
    }

    public List<PagoDTO> obtenerTodosLosPagos() {
        return pagoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public void eliminarPago(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }





    //Metodos privados

    private PagoDTO convertirADTO(Pago pago){
        return new PagoDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getFecha(),
                pago.getMetodo(),
                pago.getPedidoId(),
                pago.getEstado()
        );
    }
}
