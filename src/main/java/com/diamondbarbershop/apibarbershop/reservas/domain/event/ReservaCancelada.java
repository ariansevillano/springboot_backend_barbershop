package com.diamondbarbershop.apibarbershop.reservas.domain.event;

import com.diamondbarbershop.apibarbershop.shared.domain.event.DomainEvent;

import java.time.LocalDateTime;

/**
 * Domain Event: ocurre cuando una reserva es cancelada (por el cliente o el admin).
 *
 * Consumidores esperados:
 *   - Notificaciones → enviar email al cliente y al barbero informando la cancelación.
 *
 * Se incluye el motivo para que el email sea informativo.
 */
public record ReservaCancelada(
        Long reservaId,
        Long clienteId,
        String motivo,
        LocalDateTime occurredOn
) implements DomainEvent {}
