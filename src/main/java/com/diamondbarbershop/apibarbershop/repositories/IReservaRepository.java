package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.Barbero;
import com.diamondbarbershop.apibarbershop.models.HorarioRango;
import com.diamondbarbershop.apibarbershop.models.Reserva;
import com.diamondbarbershop.apibarbershop.models.Usuario;
import com.diamondbarbershop.apibarbershop.util.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva,Long> {
    boolean existsByBarberoAndFechaReservaAndHorarioRango(Barbero barbero, LocalDate fechaReserva, HorarioRango horarioRango);
    List<Reserva> findByFechaReservaAndHorarioRango(LocalDate fecha, HorarioRango horarioRango);
    List<Reserva> findByFechaReserva(LocalDate fecha);
    List<Reserva> findByUsuario(Usuario usuario);
    List<Reserva> findByEstado(EstadoReserva estado);
    List<Reserva> findByFechaReservaAndEstado(LocalDate fecha, EstadoReserva estado);
    List<Reserva> findByFechaReservaAndEstadoAndUsuario(LocalDate fecha, EstadoReserva estado, Usuario usuario);
    List<Reserva> findByFechaReservaAndUsuario(LocalDate fecha, Usuario usuario);
    List<Reserva> findByEstadoAndUsuario(EstadoReserva estado, Usuario usuario);
    List<Reserva> findByFechaReservaBetweenAndEstado(LocalDate fechaInicio, LocalDate fechaFin, EstadoReserva estado);

}
