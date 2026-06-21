package com.ev2.menu_service.config;

import com.ev2.menu_service.model.Plato;
import com.ev2.menu_service.repository.PlatoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PlatoRepository platoRepository;

    @Override
    public void run(String... args) {
        if (platoRepository.count() == 0) {
            Faker faker = new Faker();
            log.info(">>> Generando datos falsos para platos...");

            for (int i = 0; i < 10; i++) {
                Plato plato = new Plato();
                plato.setNombre(faker.food().dish());
                plato.setDescripcion(faker.lorem().sentence(8));
                plato.setPrecio(Double.parseDouble(
                        String.format("%.2f", faker.number().randomDouble(2, 2000, 15000))));
                plato.setDisponible(faker.bool().bool());
                platoRepository.save(plato);
            }
            log.info(">>> 10 platos generados correctamente");
        } else {
            log.info(">>> Ya existen datos, omitiendo generacion");
        }
    }
}