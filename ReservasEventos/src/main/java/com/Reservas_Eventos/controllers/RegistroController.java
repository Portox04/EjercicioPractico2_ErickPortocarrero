/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.controllers;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Usuario;
import com.Reservas_Eventos.service.RegistroService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // =========================
    // FORM NUEVO USUARIO
    // =========================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/nuevo";
    }

    // =========================
    // FORM RECORDAR CUENTA
    // =========================
    @GetMapping("/recordar")
    public String recordar(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/recordar";
    }

    // =========================
    // CREAR USUARIO
    // =========================
    @PostMapping("/crearUsuario")
    public String crearUsuario(Model model, Usuario usuarioForm)
            throws MessagingException {

        model = registroService.crearUsuario(model, usuarioForm);
        return "registro/salida";
    }

    // =========================
    // ACTIVACIÓN DESDE LINK
    // =========================
    @GetMapping("/activacion/{correo}/{clave}")
    public String activar(
            Model model,
            @PathVariable("correo") String correo,
            @PathVariable("clave") String clave) {

        model = registroService.activar(model, correo, clave);

        if (model.containsAttribute("usuario")) {
            return "registro/activa";
        } else {
            return "registro/salida";
        }
    }

    // =========================
    // ACTIVAR CUENTA FINAL
    // =========================
    @PostMapping("/activar")
    public String activarUsuario(Usuario usuarioForm) {

        registroService.activar(usuarioForm);
        return "redirect:/";
    }

    // =========================
    // RECORDAR CUENTA
    // =========================
    @PostMapping("/recordarUsuario")
    public String recordarUsuario(Model model, Usuario usuarioForm)
            throws MessagingException {

        model = registroService.recordarUsuario(model, usuarioForm);
        return "registro/salida";
    }
}
