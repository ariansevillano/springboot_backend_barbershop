package com.diamondbarbershop.apibarbershop.reservas.domain.event;

import com.diamondbarbershop.apibarbershop.shared.domain.event.DomainEvent;

import java.time.LocalDateTime;

/**
 * Domain Event: ocurre cuando el servicio fue prestado y la reserva se marca como realizada.
 *
 * Consumidores esperados:
 *   - Lógica de recompensas → sumar puntos al cliente si aplica.
 *   - Informes → actualizar métricas de ganancias del día.
 *
 * Incluye precioFinal porque es el valor que contribuye a la ganancia.
 * Este dato ya no es alcanzable de forma fiable si el precio del servicio
 * cambia posteriormente en el catálogo.
 */
public record ReservaRealizada(
        Long reservaId,
        Long clienteId,
        Long precioFinal,
        boolean recompensaAplicada,
        LocalDateTime occurredOn
) implements DomainEvent {}
