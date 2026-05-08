package com.diamondbarbershop.apibarbershop.agenda.domain.service;

import com.diamondbarbershop.apibarbershop.agenda.domain.model.HorarioBarberoInstancia;
import com.diamondbarbershop.apibarbershop.agenda.domain.port.out.ConsultarReservasActivasPort;
import com.diamondbarbershop.apibarbershop.agenda.domain.port.out.HorarioInstanciaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * DOMAIN SERVICE del Bounded Context "Agenda".
 *
 * ¿Por qué es un Domain Service y no un método de HorarioBarberoInstancia?
 *
 *   La disponibilidad depende de DOS fuentes distintas:
 *     1. ¿El barbero tiene horario activo esa fecha? → HorarioInstanciaRepository
 *     2. ¿Ese slot ya tiene una reserva activa?      → ConsultarReservasActivasPort
 *
 *   Ninguna de las dos entities tiene sentido que conozca a la otra.
 *   Forzar esta lógica dentro de HorarioBarberoInstancia violaría el
 *   Single Responsibility Principle y crearía dependencias cruzadas.
 *   → Un Domain Service es la solución correcta.
 *
 * ¿Por qué NO es un @Service de Spring aquí?
 *   Este es dominio puro — no debe depender de Spring ni de ningún framework.
 *   El @Service (Application Service) que lo usa en la capa de aplicación
 *   sí tendrá la anotación de Spring.
 *
 * HISTORIA DE USUARIO QUE RESUELVE:
 *   PB-09 — "Unificar la lógica de disponibilidad de barberos en un único servicio"
 *
 *   El problema previo: la lógica de verificar si un barbero está disponible
 *   estaba duplicada en distintos métodos y services. Cualquier cambio de regla
 *   requería actualizar múltiples lugares → alto riesgo de inconsistencia.
 *
 *   La solución: TODO el cálculo de disponibilidad pasa por ESTE servicio.
 *   Es la única fuente de verdad para esa lógica.
 */
public class CalcularDisponibilidadBarberoService {

    private final HorarioInstanciaRepository horarioInstanciaRepository;
    private final ConsultarReservasActivasPort reservasActivasPort;

    public CalcularDisponibilidadBarberoService(
            HorarioInstanciaRepository horarioInstanciaRepository,
            ConsultarReservasActivasPort reservasActivasPort
    ) {
        this.horarioInstanciaRepository = horarioInstanciaRepository;
        this.reservasActivasPort = reservasActivasPort;
    }

    /**
     * Determina si un barbero tiene disponibilidad para un slot de tiempo en una fecha.
     *
     * Reglas de negocio aplicadas (en orden):
     *   1. El barbero debe tener al menos una HorarioInstancia activa (estId=1) para esa fecha.
     *      Si no trabaja ese día, retorna false inmediatamente.
     *   2. El horarioRango específico no debe estar ocupado por una reserva CREADA o CONFIRMADA.
     *
     * @param barberoId      ID del barbero a verificar
     * @param fecha          Fecha de la cita solicitada
     * @param horarioRangoId ID del slot de tiempo deseado (ej: "09:00-09:30")
     * @return true si el barbero puede atender en ese slot
     */
    public boolean estaDisponible(Long barberoId, LocalDate fecha, Long horarioRangoId) {

        // Regla 1: ¿el barbero tiene horario activo ese día?
        List<HorarioBarberoInstancia> instancias =
                horarioInstanciaRepository.findByBarberoIdAndFecha(barberoId, fecha);

        boolean tieneHorarioActivo = instancias.stream()
                .anyMatch(HorarioBarberoInstancia::estaTrabajando);

        if (!tieneHorarioActivo) {
            return false; // el barbero descansa ese día — no hay disponibilidad
        }

        // Regla 2: ¿el slot específico ya está tomado?
        boolean slotOcupado = reservasActivasPort
                .existeReservaActiva(barberoId, fecha, horarioRangoId);

        return !slotOcupado;
    }

    /**
     * Versión extendida: devuelve el motivo de no disponibilidad.
     * Útil para mensajes de error informativos al usuario.
     */
    public DisponibilidadResultado calcularDisponibilidad(
            Long barberoId, LocalDate fecha, Long horarioRangoId
    ) {
        List<HorarioBarberoInstancia> instancias =
                horarioInstanciaRepository.findByBarberoIdAndFecha(barberoId, fecha);

        boolean tieneHorarioActivo = instancias.stream()
                .anyMatch(HorarioBarberoInstancia::estaTrabajando);

        if (!tieneHorarioActivo) {
            return DisponibilidadResultado.ocupado("El barbero no trabaja en la fecha indicada");
        }

        boolean slotOcupado = reservasActivasPort
                .existeReservaActiva(barberoId, fecha, horarioRangoId);

        if (slotOcupado) {
            return DisponibilidadResultado.ocupado("El horario seleccionado ya está reservado");
        }

        return DisponibilidadResultado.libre();
    }

    /**
     * Value Object que encapsula el resultado de la consulta de disponibilidad.
     * Evita retornar boolean primitivo cuando necesitamos también el motivo.
     *
     * NOTA: los factory methods no pueden llamarse igual que los componentes del Record
     * porque Java genera automáticamente un accessor con el mismo nombre.
     * Por eso usamos "libre()" y "ocupado()" en lugar de "disponible()" y "noDisponible()".
     */
    public record DisponibilidadResultado(boolean disponible, String motivo) {

        public static DisponibilidadResultado libre() {
            return new DisponibilidadResultado(true, null);
        }

        public static DisponibilidadResultado ocupado(String motivo) {
            return new DisponibilidadResultado(false, motivo);
        }
    }
}
