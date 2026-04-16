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
import com.Reservas_Eventos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository,
            HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByCorreoElectronicoAndCuentaActivaTrue(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        // Guardar info en sesión (opcional)
        session.setAttribute("usuarioId", usuario.getIdUsuario());
        session.setAttribute("usuarioNombre", usuario.getNombreCompleto());

        // Roles
        var roles = java.util.List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getRolAsignado().getNombreRol()
                )
        );

        return new User(
                usuario.getCorreoElectronico(),
                usuario.getPassword(),
                roles
        );
    }
}
