/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Reservas_Eventos.repository;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Evento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Buscar por estado
    List<Evento> findByEventoActivo(boolean eventoActivo);

    // Buscar por nombre (LIKE)
    List<Evento> findByNombreEventoContainingIgnoreCase(String nombreEvento);

    // Buscar por rango de fechas
    List<Evento> findByFechaEventoBetween(LocalDate inicio, LocalDate fin);
}
