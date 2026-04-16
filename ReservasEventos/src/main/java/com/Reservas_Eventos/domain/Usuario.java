/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.domain;

/**
 *
 * @author porto
 */
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idUsuario;

    @NotBlank
    @Column(name = "nombre", length = 150)
    private String nombreCompleto;

    @Email
    @NotBlank
    @Column(name = "email", unique = true, length = 200)
    private String correoElectronico;

    @Column(name = "password", length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rolAsignado;

    @Column(name = "activo")
    private boolean cuentaActiva;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void antesDeGuardar() {
        this.fechaRegistro = LocalDateTime.now();
        this.cuentaActiva = true;
    }
}