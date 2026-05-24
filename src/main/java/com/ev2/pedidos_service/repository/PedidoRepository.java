package com.ev2.pedidos_service.repository;

import com.ev2.pedidos_service.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Trae pedidos por estado (PENDIENTE, EN_PROCESO, ENTREGADO)
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> findByEstado(@Param("estado") String estado);

    // Trae todos los pedidos de una mesa
    @Query("SELECT p FROM Pedido p WHERE p.mesa = :mesa")
    List<Pedido> findByMesa(@Param("mesa") Integer mesa);

    // Trae todos los pedidos de un plato específico
    @Query("SELECT p FROM Pedido p WHERE p.platoId = :platoId")
    List<Pedido> findByPlatoId(@Param("platoId") Long platoId);
}