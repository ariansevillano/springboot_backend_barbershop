package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_servicios")
public class TipoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoServicio_Id")
    private Long tipoServicio_id;
    @Column(nullable = false)
    private String nombre;
}
