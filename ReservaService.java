package com.restaurant.mesa.config;

import com.restaurant.mesa.model.Mesa;
import com.restaurant.mesa.model.Reserva;
import com.restaurant.mesa.repository.MesaRepository;
import com.restaurant.mesa.repository.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@Component
public class DataInitializer implements CommandLineRunner {

    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

    public DataInitializer(MesaRepository mesaRepository, ReservaRepository reservaRepository) {
        this.mesaRepository = mesaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) {
        if (mesaRepository.count() > 0) {
            log.info(">>> Mesas y reservas ya cargadas. Se omite inicialización.");
            return;}

        log.info(">>> Cargando datos iniciales...");

        // Poblar mesas
        mesaRepository.save(new Mesa(null, 1, "libre", null));
        mesaRepository.save(new Mesa(null, 2, "libre", null));
        mesaRepository.save(new Mesa(null, 3, "libre", null));
        mesaRepository.save(new Mesa(null, 4, "libre", null));

        log.info(">>> Mesas y reservas iniciales cargadas OK.");
    }
}
