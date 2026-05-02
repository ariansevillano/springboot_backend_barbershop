package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.HorarioRango;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorarioRangoRepository extends JpaRepository<HorarioRango,Long> {

}
