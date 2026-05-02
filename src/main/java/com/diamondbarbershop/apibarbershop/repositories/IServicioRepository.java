package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Long> {

}
