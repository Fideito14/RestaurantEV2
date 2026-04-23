package com.mesas_backend.mesas.controller;


import com.mesas_backend.mesas.model.mesa;
import com.mesas_backend.mesas.service.mesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mesas")
public class mesaController {
    @Autowired
    private mesaService mesaService;

    //endpoint para obtener todas las mesas
    @GetMapping
    public ResponseEntity<List<mesa>> obtenerMesas(){
        List<mesa> mesas= mesaService.getMesas();
        if (mesas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<mesa> obtenerMesa(@PathVariable Long id){
        try {
            mesa mesa = mesaService.getMesa(id);
            return ResponseEntity.ok(mesa);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    //endpoint para agregar una mesa
    @PostMapping
    public ResponseEntity<mesa> guardarMesa(@RequestBody mesa nueva){
        return ResponseEntity.status(201).body(mesaService.saveMesa(nueva));
    }
}
