package com.restaurant.pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto", nullable = false)
    private double monto;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "metodo", nullable = false, length = 20)
    private String metodo;

    @Column(name = "pedido_id")
    private Long pedidoId; //FK hacia pedido

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; //pendiente, pagado, cancelado
}
