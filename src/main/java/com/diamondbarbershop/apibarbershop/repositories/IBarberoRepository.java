package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBarberoRepository extends JpaRepository<Barbero, Long> {
    Optional<Barbero> findByEstado(Integer estado);

    Optional<Barbero> findByNombre(String nombre);
}
