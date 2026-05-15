package com.diamondbarbershop.apibarbershop.shared.domain.event;

import java.time.LocalDateTime;

/**
 * Interfaz base para todos los Domain Events del sistema BarberHub.
 *
 * Un Domain Event representa un HECHO DEL PASADO que le importa al negocio.
 * Por eso:
 *   - Se nombra en tiempo pasado:  ReservaCreada, ReservaConfirmada...
 *   - Es INMUTABLE: lo que ocurrió, ocurrió — no se modifica.
 *   - Lo EMITE el Aggregate Root cuando cambia su estado.
 *   - Lo CONSUME quien necesite reaccionar (mismo u otro Bounded Context).
 *
 * Ejemplo de flujo:
 *   Reserva.confirmar()
 *     → estado = CONFIRMADA
 *     → emite ReservaConfirmada (queda en lista interna)
 *   GestionarReservaApplicationService.confirmar()
 *     → llama reservaRepository.save(reserva)
 *     → extrae reserva.pullEvents()
 *     → publica cada evento en el EventBus
 *   EmailEventHandler (suscriptor)
 *     → recibe ReservaConfirmada
 *     → envía email al cliente
 */
public interface DomainEvent {

    /**
     * Momento exacto en que ocurrió el evento.
     * Permite reconstruir la historia del dominio.
     */
    LocalDateTime occurredOn();
}
