package com.restaurant.pagos.client;

import com.restaurant.pagos.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedidos-service",url = "${ms.pedidos.url}")
public interface PedidoClient {

    @GetMapping("/api/pedidos/{id}")
    PedidoDTO getPedidoById(@PathVariable Long id);

}
