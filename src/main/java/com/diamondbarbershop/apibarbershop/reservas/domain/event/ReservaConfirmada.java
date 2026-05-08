package com.diamondbarbershop.apibarbershop.reservas.domain.event;

import com.diamondbarbershop.apibarbershop.shared.domain.event.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain Event: ocurre cuando el admin o barbero confirma una reserva.
 *
 * Consumidores esperados:
 *   - Notificaciones → enviar email al cliente confirmando su cita.
 */
public record ReservaConfirmada(
        Long reservaId,
        Long clienteId,
        LocalDate fechaReserva,
        LocalDateTime occurredOn
) implements DomainEvent {}
