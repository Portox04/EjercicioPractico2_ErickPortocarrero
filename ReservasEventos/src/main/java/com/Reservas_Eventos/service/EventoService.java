/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos.service;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.domain.Evento;
import com.Reservas_Eventos.repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> getEventos(boolean activos) {
        if (activos) {
            return eventoRepository.findByEventoActivo(true);
        }
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Evento> getEvento(Long id) {
        return eventoRepository.findById(id);
    }

    @Transactional
    public void save(Evento evento) {

        if (evento.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException("Capacidad inválida.");
        }

        eventoRepository.save(evento);
    }

    @Transactional
    public void delete(Long id) {

        if (!eventoRepository.existsById(id)) {
            throw new IllegalArgumentException("Evento no existe.");
        }

        try {
            eventoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar.", e);
        }
    }


    @Transactional(readOnly = true)
    public List<Evento> buscarPorEstado(boolean activo) {
        return eventoRepository.findByEventoActivo(activo);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorNombre(String nombre) {
        return eventoRepository.findByNombreEventoContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorFechas(LocalDate inicio, LocalDate fin) {
        return eventoRepository.findByFechaEventoBetween(inicio, fin);
    }
}
