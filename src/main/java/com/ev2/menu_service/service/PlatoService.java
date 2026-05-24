package com.ev2.menu_service.service;

import com.ev2.menu_service.dto.PlatoRequestDTO;
import com.ev2.menu_service.dto.PlatoResponseDTO;
import com.ev2.menu_service.model.Plato;
import com.ev2.menu_service.repository.PlatoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class PlatoService {

    private final PlatoRepository platoRepository;

    private PlatoResponseDTO mapToDTO(Plato p) {
        return new PlatoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.isDisponible()
        );
    }

    public List<PlatoResponseDTO> obtenerTodos() {
        log.info(">>> Obteniendo todos los platos <<<");
        return platoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PlatoResponseDTO> obtenerDisponibles() {
        log.info(">>> Obteniendo platos disponibles <<<");
        return platoRepository.findAllDisponibles()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PlatoResponseDTO> buscarPorNombre(String nombre) {
        log.info(">>> Buscando platos por nombre : {}", nombre);
        return platoRepository.buscarPorNombre(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PlatoResponseDTO> obtenerPorId(Long id) {
        log.info(">>> Buscando plato por id: {}", id);
        return platoRepository.findById(id).map(this::mapToDTO);
    }

    public PlatoResponseDTO guardar(PlatoRequestDTO dto) {
        log.info(">>> Creando plato: {}", dto.getNombre());
        Plato plato = new Plato(
                null,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getDisponible()
        );
        return mapToDTO(platoRepository.save(plato));
    }

    public Optional<PlatoResponseDTO> actualizar(Long id, PlatoRequestDTO dto) {
        log.info(">>> Actualizando plato por id: {}", id);
        return platoRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            existente.setDescripcion(dto.getDescripcion());
            existente.setPrecio(dto.getPrecio());
            existente.setDisponible(dto.getDisponible());
            return mapToDTO(platoRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        log.info(">>> Eliminando plato por id: {}", id);
        platoRepository.deleteById(id);
    }
}