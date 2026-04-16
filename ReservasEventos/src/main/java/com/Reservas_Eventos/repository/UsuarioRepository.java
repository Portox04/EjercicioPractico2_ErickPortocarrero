/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Reservas_Eventos.repository;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Usuario;
import com.Reservas_Eventos.domain.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoElectronicoAndCuentaActivaTrue(String correo);

    List<Usuario> findByCuentaActivaTrue();

    Optional<Usuario> findByCorreoElectronico(String correo);

    Optional<Usuario> findByCorreoElectronicoAndPassword(String correo, String password);

    boolean existsByCorreoElectronico(String correo);
}
