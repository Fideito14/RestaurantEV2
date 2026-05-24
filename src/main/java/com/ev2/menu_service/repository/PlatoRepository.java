package com.ev2.menu_service.repository;

import com.ev2.menu_service.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    // Trae solo los platos disponibles
    @Query("SELECT p FROM Plato p WHERE p.disponible = true")
    List<Plato> findAllDisponibles();

    // Búsqueda parcial por nombre, sin importar mayúsculas
    @Query("SELECT p FROM Plato p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Plato> buscarPorNombre(@Param("nombre") String nombre);
}