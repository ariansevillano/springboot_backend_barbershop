package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValoracionRepository extends JpaRepository<Valoracion,Long> {
}
