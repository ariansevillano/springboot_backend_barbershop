package com.diamondbarbershop.apibarbershop.repositories;

import com.diamondbarbershop.apibarbershop.models.HorarioBarberoBase;
import com.diamondbarbershop.apibarbershop.util.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHorarioBarberoBaseRepository extends JpaRepository<HorarioBarberoBase,Long> {

    List<HorarioBarberoBase> findByDia(DiaSemana dia);
}
