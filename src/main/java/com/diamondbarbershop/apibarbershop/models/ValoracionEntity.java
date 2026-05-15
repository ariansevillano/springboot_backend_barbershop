package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "valoraciones")
public class ValoracionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valoracion_id")
    private Long valoracion_id;
    private Integer valoracion;
    private Boolean util;
    private String mensaje;
    @ManyToOne
    @JoinColumn( name = "usuario_id",nullable = false)
    private Usuario usuario;
    private Integer estado;
}
