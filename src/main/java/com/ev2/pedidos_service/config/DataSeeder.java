package com.ev2.pedidos_service.config;

import com.ev2.pedidos_service.model.Pedido;
import com.ev2.pedidos_service.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        if (pedidoRepository.count() == 0) {
            Faker faker = new Faker();
            log.info(">>> Generando datos falsos para pedidos...");

            String[] estados = {"PENDIENTE", "EN_PROCESO", "ENTREGADO"};

            for (int i = 0; i < 10; i++) {
                Pedido pedido = new Pedido();
                pedido.setPlatoId((long) faker.number().numberBetween(1, 6));
                pedido.setCantidad(faker.number().numberBetween(1, 5));
                pedido.setMesa(faker.number().numberBetween(1, 10));
                pedido.setEstado(estados[faker.number().numberBetween(0, 3)]);
                pedidoRepository.save(pedido);
            }
            log.info(">>> 10 pedidos generados correctamente");
        } else {
            log.info(">>> Ya existen datos, omitiendo generacion");
        }
    }
}