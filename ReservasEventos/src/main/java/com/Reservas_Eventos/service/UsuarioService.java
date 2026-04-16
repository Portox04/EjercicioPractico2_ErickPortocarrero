/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.service;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Rol;
import com.Reservas_Eventos.domain.Usuario;
import com.Reservas_Eventos.repository.RolRepository;
import com.Reservas_Eventos.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activos) {
        if (activos) {
            return usuarioRepository.findByCuentaActivaTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreoElectronico(correo);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorCorreoYPassword(String correo, String password) {
        return usuarioRepository.findByCorreoElectronicoAndPassword(correo, password);
    }

    @Transactional(readOnly = true)
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }

    @Transactional
    public void guardar(Usuario usuario, boolean encriptarClave) {

        // Validar duplicado
        Optional<Usuario> existente = usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico());

        if (existente.isPresent()
                && (usuario.getIdUsuario() == null
                || !existente.get().getIdUsuario().equals(usuario.getIdUsuario()))) {

            throw new DataIntegrityViolationException("Correo ya en uso");
        }

        boolean esNuevo = (usuario.getIdUsuario() == null);

        if (esNuevo) {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password requerido");
            }

            usuario.setPassword(
                    encriptarClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword()
            );

            usuario.setCuentaActiva(false);
        } else {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                Usuario actual = usuarioRepository.findById(usuario.getIdUsuario())
                        .orElseThrow();
                usuario.setPassword(actual.getPassword());
            } else {
                usuario.setPassword(
                        encriptarClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword()
                );
            }
        }

        usuario = usuarioRepository.save(usuario);


        if (esNuevo) {
            asignarRol(usuario, "USER");
        }
    }

    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void asignarRol(Usuario usuario, String nombreRol) {
        Optional<Rol> rolOpt = rolRepository.findByNombreRol(nombreRol);

        if (rolOpt.isEmpty()) {
            throw new RuntimeException("Rol no existe");
        }

        usuario.setRolAsignado(rolOpt.get());
        usuarioRepository.save(usuario);
    }
}
