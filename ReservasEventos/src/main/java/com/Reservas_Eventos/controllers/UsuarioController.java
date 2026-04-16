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
import com.Reservas_Eventos.service.RolService;
import com.Reservas_Eventos.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @Autowired
    private RolService rolService;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("roles", rolService.getRoles());
        return "usuario/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("error", "Error en los datos");

            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario/listado";
            }

            return "redirect:/usuario/modificar/" + usuario.getIdUsuario();
        }

        try {
            usuarioService.guardar(usuario, true);

            redirectAttributes.addFlashAttribute("todoOk", "Usuario guardado correctamente");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "El correo ya está en uso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado");
        }

        return "redirect:/usuario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long idUsuario,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.delete(idUsuario);

            redirectAttributes.addFlashAttribute("todoOk", "Usuario eliminado");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario");
        }

        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable Long idUsuario,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuario/listado";
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.getRoles());

        return "usuario/modifica";
    }
}
