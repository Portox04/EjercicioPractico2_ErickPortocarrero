/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.service;

import com.Reservas_Eventos.domain.Rol;
import com.Reservas_Eventos.repository.RolRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author porto
 */
@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    public Rol getRol(Long idRol) {
        return rolRepository.findById(idRol).orElse(null);
    }
}

