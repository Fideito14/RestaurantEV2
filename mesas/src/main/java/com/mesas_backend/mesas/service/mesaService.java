package com.mesas_backend.mesas.service;

import com.mesas_backend.mesas.model.mesa;
import com.mesas_backend.mesas.repository.mesaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class mesaService {
    @Autowired
    private mesaRepository mesaRepository;

    //metodo para obtener todas las mesas
    public List<mesa> getMesas(){ return mesaRepository.findAll();}

    //metodo para obtener todas las mesas por id
    public mesa getMesa(Long idMesa){
        return mesaRepository.findById(idMesa).orElseThrow(()-> new RuntimeException("Mesa no encontrada"));
    }

    //metodo para crear una mesa
    public mesa saveMesa(mesa mesa){
        return mesaRepository.save(mesa);
    }
}
