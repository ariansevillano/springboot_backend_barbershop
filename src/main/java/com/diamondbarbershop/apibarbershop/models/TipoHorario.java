package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_horarios")
public class TipoHorario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoHorario_id")
    private Long id;
    @Column(nullable = false)
    private String nombre;
}
