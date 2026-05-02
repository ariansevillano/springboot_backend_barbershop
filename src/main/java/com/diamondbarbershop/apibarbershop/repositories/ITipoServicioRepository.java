package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITipoServicioRepository extends JpaRepository<TipoServicio, Long> {
}
