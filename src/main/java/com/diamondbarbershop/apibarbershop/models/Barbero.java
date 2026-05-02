package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "barberos")
public class Barbero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barbero_id")
    private Long barbero_id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Integer estado;
    @Column(nullable = true)
    private String urlBarbero;
}
