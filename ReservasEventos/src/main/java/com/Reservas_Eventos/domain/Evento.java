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
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idEvento;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", length = 150)
    private String nombreEvento;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcionEvento;

    @NotNull
    @Column(name = "fecha")
    private LocalDate fechaEvento;

    @NotNull
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    @Column(name = "capacidad")
    private Integer capacidadMaxima;

    @Column(name = "activo")
    private boolean eventoActivo;
}
