package com.diamondbarbershop.apibarbershop.models;

import com.diamondbarbershop.apibarbershop.util.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_barbero_instancias")
public class HorarioBarberoInstancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioBarberoInstancia_id")
    private Long horarioBarberoInstancia_id;
    @ManyToOne
    @JoinColumn(name = "barbero_id",nullable = false)
    private Barbero barbero;
    @ManyToOne
    @JoinColumn(name = "tipoHorario_id",nullable = false)
    private TipoHorario tipoHorario;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;
    @Column(nullable = false)
    private LocalDate fecha;
    private Integer est_id;
}
