package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_rangos")
public class HorarioRango {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioRango_id")
    private Long horarioRango_id;
    @Column(nullable = false)
    private String rango;
    @ManyToOne
    @JoinColumn( name = "tipoHorario_id",nullable = false)
    private TipoHorario tipoHorario;
}
