package com.restaurant.pagos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {

    private Long id;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Double monto;

    private LocalDateTime fecha;

    @NotBlank(message = "El metodo de pago es obligatorio")
    @Pattern(regexp = "efectivo|tarjeta", message = "El metodo de pago debe ser 'efectivo' o 'tarjeta'")
    private String metodo;

    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "pendiente|pagado|cancelado", message = "El estado debe ser 'pendiente', 'pagado' o 'cancelado'")
    private String estado;
}
