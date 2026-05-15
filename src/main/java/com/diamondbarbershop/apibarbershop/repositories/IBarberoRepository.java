package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.Barbero;
import com.diamondbarbershop.apibarbershop.reservas.domain.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBarberoRepository extends JpaRepository<Barbero, Long> {
    Optional<Barbero> findByEstado(Integer estado);

    Optional<Barbero> findByNombre(String nombre);
}
