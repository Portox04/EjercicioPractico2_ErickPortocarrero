/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.service;

/**
 *
 * @author porto
 */

import com.Reservas_Eventos.domain.Usuario;
import jakarta.mail.MessagingException;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import org.springframework.ui.Model;

@Service
public class RegistroService {

    private final CorreoService correoService;
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public RegistroService(CorreoService correoService,
            UsuarioService usuarioService,
            MessageSource messageSource) {
        this.correoService = correoService;
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    // =========================
    // ACTIVAR DESDE CORREO
    // =========================
    public Model activar(Model model, String correoElectronico, String clave) {

        Optional<Usuario> usuario
                = usuarioService.getUsuarioPorCorreoYPassword(correoElectronico, clave);

        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
        } else {
            model.addAttribute("titulo",
                    messageSource.getMessage("registro.activar", null, Locale.getDefault()));
            model.addAttribute("mensaje",
                    messageSource.getMessage("registro.activar.error", null, Locale.getDefault()));
        }

        return model;
    }

    // =========================
    // ACTIVAR CUENTA FINAL
    // =========================
    public void activar(Usuario usuario) {
        usuario.setCuentaActiva(true);
        usuarioService.guardar(usuario, true);
    }

    // =========================
    // CREAR USUARIO
    // =========================
    public Model crearUsuario(Model model, Usuario usuario) throws MessagingException {

        String mensaje;

        try {
            String clave = demeClave();

            usuario.setPassword(clave);
            usuario.setCuentaActiva(false);

            usuarioService.guardar(usuario, false);

            enviaCorreoActivar(usuario, clave);

            mensaje = String.format(
                    messageSource.getMessage("registro.mensaje.activacion.ok", null, Locale.getDefault()),
                    usuario.getCorreoElectronico()
            );

        } catch (MessagingException | NoSuchMessageException e) {

            mensaje = String.format(
                    messageSource.getMessage("registro.mensaje.usuario.o.correo", null, Locale.getDefault()),
                    usuario.getCorreoElectronico()
            );
        }

        model.addAttribute("titulo",
                messageSource.getMessage("registro.activar", null, Locale.getDefault()));
        model.addAttribute("mensaje", mensaje);

        return model;
    }

    // =========================
    // RECORDAR USUARIO
    // =========================
    public Model recordarUsuario(Model model, Usuario usuario) throws MessagingException {

        String mensaje;

        Optional<Usuario> usuarioOpt
                = usuarioService.getUsuarioPorCorreo(usuario.getCorreoElectronico());

        if (usuarioOpt.isPresent()) {

            usuario = usuarioOpt.get();

            String clave = demeClave();

            usuario.setPassword(clave);
            usuario.setCuentaActiva(false);

            usuarioService.guardar(usuario, false);

            enviaCorreoRecordar(usuario, clave);

            mensaje = String.format(
                    messageSource.getMessage("registro.mensaje.recordar.ok", null, Locale.getDefault()),
                    usuario.getCorreoElectronico()
            );

        } else {
            mensaje = String.format(
                    messageSource.getMessage("registro.mensaje.usuario.o.correo", null, Locale.getDefault()),
                    usuario.getCorreoElectronico()
            );
        }

        model.addAttribute("titulo",
                messageSource.getMessage("registro.activar", null, Locale.getDefault()));
        model.addAttribute("mensaje", mensaje);

        return model;
    }

    // =========================
    // GENERAR CLAVE
    // =========================
    private String demeClave() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder clave = new StringBuilder();

        for (int i = 0; i < 40; i++) {
            clave.append(chars.charAt((int) (Math.random() * chars.length())));
        }

        return clave.toString();
    }

    @Value("${servidor.http}")
    private String servidor;

    // =========================
    // CORREO ACTIVACIÓN
    // =========================
    private void enviaCorreoActivar(Usuario usuario, String clave) throws MessagingException {

        String mensaje = messageSource.getMessage("registro.correo.activar", null, Locale.getDefault());

        mensaje = String.format(
                mensaje,
                usuario.getNombreCompleto(),
                servidor,
                usuario.getCorreoElectronico(),
                clave
        );

        String asunto = messageSource.getMessage("registro.mensaje.activacion", null, Locale.getDefault());

        correoService.enviarCorreoHtml(usuario.getCorreoElectronico(), asunto, mensaje);
    }

    // =========================
    // CORREO RECORDAR
    // =========================
    private void enviaCorreoRecordar(Usuario usuario, String clave) throws MessagingException {

        String mensaje = messageSource.getMessage("registro.correo.recordar", null, Locale.getDefault());

        mensaje = String.format(
                mensaje,
                usuario.getNombreCompleto(),
                servidor,
                usuario.getCorreoElectronico(),
                clave
        );

        String asunto = messageSource.getMessage("registro.mensaje.recordar", null, Locale.getDefault());

        correoService.enviarCorreoHtml(usuario.getCorreoElectronico(), asunto, mensaje);
    }
}
