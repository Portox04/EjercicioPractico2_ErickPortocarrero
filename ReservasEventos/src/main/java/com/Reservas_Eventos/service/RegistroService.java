package com.Reservas_Eventos.service;

import com.Reservas_Eventos.domain.Usuario;
import jakarta.mail.MessagingException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RegistroService {

    private final CorreoService correoService;
    private final UsuarioService usuarioService;

    public RegistroService(CorreoService correoService,
                           UsuarioService usuarioService) {
        this.correoService = correoService;
        this.usuarioService = usuarioService;
    }

    // =========================
    // ACTIVAR DESDE CORREO
    // =========================
    public Model activar(Model model, String correoElectronico, String clave) {

        Optional<Usuario> usuario =
                usuarioService.getUsuarioPorCorreoYPassword(correoElectronico, clave);

        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
        } else {
            model.addAttribute("titulo", "Activación de cuenta");
            model.addAttribute("mensaje", "Código o usuario inválido para activación");
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

            mensaje = "Usuario registrado correctamente. Revisa tu correo: "
                    + usuario.getCorreoElectronico();

        } catch (Exception e) {

            mensaje = "Error: el usuario o correo ya existe: "
                    + usuario.getCorreoElectronico();
        }

        model.addAttribute("titulo", "Activación de cuenta");
        model.addAttribute("mensaje", mensaje);

        return model;
    }

    // =========================
    // RECORDAR USUARIO
    // =========================
    public Model recordarUsuario(Model model, Usuario usuario) throws MessagingException {

        String mensaje;

        Optional<Usuario> usuarioOpt =
                usuarioService.getUsuarioPorCorreo(usuario.getCorreoElectronico());

        if (usuarioOpt.isPresent()) {

            usuario = usuarioOpt.get();

            String clave = demeClave();

            usuario.setPassword(clave);
            usuario.setCuentaActiva(false);

            usuarioService.guardar(usuario, false);

            enviaCorreoRecordar(usuario, clave);

            mensaje = "Se envió una nueva contraseña al correo: "
                    + usuario.getCorreoElectronico();

        } else {

            mensaje = "No existe un usuario con ese correo: "
                    + usuario.getCorreoElectronico();
        }

        model.addAttribute("titulo", "Recordar usuario");
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

    // servidor
    private String servidor = "http://localhost:8080";

    // =========================
    // CORREO ACTIVACIÓN
    // =========================
    private void enviaCorreoActivar(Usuario usuario, String clave) throws MessagingException {

        String mensaje = """
                Hola %s,
                
                Para activar tu cuenta ingresa a:
                %s/registro/activar
                
                Usuario: %s
                Clave: %s
                """.formatted(
                usuario.getNombreCompleto(),
                servidor,
                usuario.getCorreoElectronico(),
                clave
        );

        String asunto = "Activación de cuenta";

        correoService.enviarCorreoHtml(usuario.getCorreoElectronico(), asunto, mensaje);
    }

    // =========================
    // CORREO RECORDAR
    // =========================
    private void enviaCorreoRecordar(Usuario usuario, String clave) throws MessagingException {

        String mensaje = """
                Hola %s,
                
                Tu nueva contraseña es:
                %s
                
                Usuario: %s
                """.formatted(
                usuario.getNombreCompleto(),
                clave,
                usuario.getCorreoElectronico()
        );

        String asunto = "Recuperación de cuenta";

        correoService.enviarCorreoHtml(usuario.getCorreoElectronico(), asunto, mensaje);
    }
}