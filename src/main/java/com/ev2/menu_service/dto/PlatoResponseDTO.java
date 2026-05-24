package com.ev2.menu_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private boolean disponible;
}