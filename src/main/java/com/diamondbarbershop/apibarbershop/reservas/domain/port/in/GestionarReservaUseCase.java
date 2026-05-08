package com.diamondbarbershop.apibarbershop.reservas.domain.port.in;

/**
 * Puerto de ENTRADA — caso de uso "Gestionar estado de una Reserva".
 *
 * Agrupa las operaciones de cambio de estado que no son creación:
 *   - Confirmar (admin/barbero aprueba la cita)
 *   - Marcar como realizada (el servicio fue prestado)
 *   - Cancelar (cliente o admin cancela)
 */
public interface GestionarReservaUseCase {

    void confirmar(Long reservaId);

    void marcarComoRealizada(Long reservaId);

    void cancelar(Long reservaId, String motivo);
}
