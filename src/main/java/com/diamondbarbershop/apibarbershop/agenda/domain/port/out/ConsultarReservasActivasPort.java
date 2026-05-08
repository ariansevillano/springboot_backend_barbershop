package com.diamondbarbershop.apibarbershop.agenda.domain.port.out;

import java.time.LocalDate;

/**
 * Puerto de salida del contexto Agenda — Anti-Corruption Layer hacia Reservas.
 *
 * ¿Por qué existe este puerto?
 *   Para calcular disponibilidad, Agenda necesita saber si un slot de tiempo
 *   ya está ocupado por una reserva activa. Pero Agenda NO debe depender
 *   directamente del BC Reservas (eso crearía acoplamiento entre contextos).
 *
 *   Solución ACL:
 *   - Agenda define ESTA interfaz con los términos de su propio lenguaje.
 *   - La implementación vive en infraestructura y consulta el repositorio de Reservas.
 *   - Si el BC Reservas cambia internamente, solo cambia el adaptador, no este puerto.
 *
 * Implementación esperada (Sprint 2):
 *   ConsultarReservasActivasAdapter → IReservaJpaRepository.existsByBarberoIdAndFechaAndHorarioRangoIdAndEstadoIn(...)
 */
public interface ConsultarReservasActivasPort {

    /**
     * Verifica si existe una reserva activa (CREADA o CONFIRMADA) para ese
     * barbero, fecha y slot de tiempo.
     *
     * "Activa" significa que todavía puede ocurrir — no cancelada, no realizada.
     */
    boolean existeReservaActiva(Long barberoId, LocalDate fecha, Long horarioRangoId);
}
