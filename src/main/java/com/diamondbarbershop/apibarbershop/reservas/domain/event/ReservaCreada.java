package com.diamondbarbershop.apibarbershop.reservas.domain.event;

import com.diamondbarbershop.apibarbershop.shared.domain.event.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain Event: ocurre cuando un cliente crea una nueva reserva.
 *
 * Consumidores esperados:
 *   - Notificaciones → enviar email al barbero avisando de la nueva cita.
 *
 * Usamos Java Record porque los eventos son inmutables por naturaleza:
 * lo que pasó no se puede cambiar, por lo tanto sus datos tampoco.
 */
public record ReservaCreada(
        Long reservaId,
        Long barberoId,
        Long clienteId,
        Long servicioId,
        LocalDate fechaReserva,
        Long horarioRangoId,
        LocalDateTime occurredOn
) implements DomainEvent {}
