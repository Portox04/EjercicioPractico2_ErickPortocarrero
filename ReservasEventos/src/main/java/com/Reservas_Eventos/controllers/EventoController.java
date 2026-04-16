/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.controllers;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Evento;
import com.Reservas_Eventos.service.EventoService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final MessageSource messageSource;

    public EventoController(EventoService eventoService,
                            MessageSource messageSource) {
        this.eventoService = eventoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var eventos = eventoService.getEventos(false);
        model.addAttribute("eventos", eventos);
        model.addAttribute("totalEventos", eventos.size());
        model.addAttribute("evento", new Evento());
        return "/eventos/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Evento evento,
                          BindingResult errors,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (errors.hasErrors()) {
            return "/eventos/listado";
        }

        try {
            eventoService.save(evento);

            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/eventos/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long id,
                           RedirectAttributes redirectAttributes) {

        try {
            eventoService.delete(id);

            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo eliminar el evento.");
        }

        return "redirect:/eventos/listado";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        Optional<Evento> eventoOpt = eventoService.getEvento(id);

        if (eventoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
            return "redirect:/eventos/listado";
        }

        model.addAttribute("evento", eventoOpt.get());

        return "/eventos/modifica";
    }
}