package com.diamondbarbershop.apibarbershop.reservas.application;

import com.diamondbarbershop.apibarbershop.reservas.domain.model.Reserva;
import com.diamondbarbershop.apibarbershop.reservas.domain.port.in.GestionarReservaUseCase;
import com.diamondbarbershop.apibarbershop.reservas.domain.port.out.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Gestionar estados de una Reserva.
 *
 * Patrón: el Application Service carga el aggregate, delega la lógica
 * al dominio y persiste el resultado.
 * Nunca pone lógica de negocio aquí — eso es responsabilidad del aggregate.
 */
@Service
@RequiredArgsConstructor
public class GestionarReservaApplicationService implements GestionarReservaUseCase {

    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public void confirmar(Long reservaId) {
        Reserva reserva = cargar(reservaId);

        // Dominio valida la transición: solo CREADA → CONFIRMADA
        reserva.confirmar();

        Reserva guardada = reservaRepository.save(reserva);
        publicarEventos(guardada);
    }

    @Override
    @Transactional
    public void marcarComoRealizada(Long reservaId) {
        Reserva reserva = cargar(reservaId);

        // Dominio valida la transición: solo CONFIRMADA → REALIZADA
        reserva.marcarComoRealizada();

        Reserva guardada = reservaRepository.save(reserva);
        publicarEventos(guardada);
    }

    @Override
    @Transactional
    public void cancelar(Long reservaId, String motivo) {
        Reserva reserva = cargar(reservaId);

        // Dominio valida: no se puede cancelar una REALIZADA
        reserva.cancelar(motivo);

        Reserva guardada = reservaRepository.save(reserva);
        publicarEventos(guardada);
    }

    // ── Helpers privados ─────────────────────────────────────────────────────────

    private Reserva cargar(Long reservaId) {
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + reservaId));
    }

    private void publicarEventos(Reserva reserva) {
        reserva.pullEvents().forEach(evento ->
                System.out.println("[DomainEvent emitido] " + evento.getClass().getSimpleName())
        );
    }
}
