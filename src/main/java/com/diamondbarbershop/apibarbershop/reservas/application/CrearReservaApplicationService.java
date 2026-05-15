package com.diamondbarbershop.apibarbershop.reservas.application;

import com.diamondbarbershop.apibarbershop.reservas.domain.model.Precio;
import com.diamondbarbershop.apibarbershop.reservas.domain.model.Reserva;
import com.diamondbarbershop.apibarbershop.reservas.domain.port.in.CrearReservaUseCase;
import com.diamondbarbershop.apibarbershop.reservas.domain.port.out.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Caso de uso: Crear Reserva.
 *
 * Application Service — orquesta el flujo sin tener lógica de negocio propia.
 * La lógica está en el aggregate Reserva (dominio).
 *
 * Responsabilidades:
 *   1. Verificar que el slot esté disponible (barbero + fecha + rango).
 *   2. Delegar la creación al aggregate Reserva.crear().
 *   3. Persistir a través del puerto ReservaRepository.
 *   4. Publicar los Domain Events emitidos por el aggregate.
 */
@Service
@RequiredArgsConstructor
public class CrearReservaApplicationService implements CrearReservaUseCase {

    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public Long crear(CrearReservaCommand command) {

        // 1. Verificar disponibilidad: ¿ya hay reserva activa para ese barbero/fecha/rango?
        boolean espacioOcupado = reservaRepository
                .findByBarberoIdAndFecha(command.barberoId(),
                        command.fechaReserva())
                .stream()
                .anyMatch( reserva -> reserva.getHorarioRangoId().equals(command.horarioRangoId())
                && !reserva.estaCancelada());

        if (espacioOcupado) {
            throw new RuntimeException(
                    "El barbero ya tiene una reserva confirmada en ese horario"
            );
        }

        // 2. El dominio crea el aggregate con sus invariantes y emite el evento
        Reserva reserva = Reserva.crear(
                command.barberoId(),
                command.clienteId(),
                command.servicioId(),
                command.horarioRangoId(),
                new Precio(command.precioServicio()),
                command.fechaReserva(),
                command.adicionales()
        );

        // 3. Persistir a través del puerto (no del JPA directamente)
        Reserva guardada = reservaRepository.save(reserva);

        // 4. Publicar eventos emitidos por el aggregate
        //    Por ahora solo log — en Sprint posterior conectaremos el EventBus real
        guardada.pullEvents().forEach(
                event -> System.out.println("[Domain event emitido] " + event
                        .getClass().getSimpleName()));

        return guardada.getId();
    }
}
