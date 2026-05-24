package com.ev2.pedidos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private Long platoId;
    private Integer cantidad;
    private Integer mesa;
    private String estado;
}