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
import com.Reservas_Eventos.service.UsuarioService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final UsuarioService usuarioService;

    public ConsultaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {

        var usuarios = usuarioService.getUsuarios(true); // solo activos
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());

        return "consultas/usuarios";
    }

    @GetMapping("/usuario/{idUsuario}")
    public String verUsuario(@PathVariable Long idUsuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/consultas/usuarios";
        }

        model.addAttribute("usuario", usuarioOpt.get());

        return "consultas/detalle_usuario";
    }


    @PostMapping("/buscar")
    public String buscarPorCorreo(@RequestParam String correo,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.getUsuarioPorCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/consultas/usuarios";
        }

        model.addAttribute("usuario", usuarioOpt.get());

        return "consultas/detalle_usuario";
    }
}
