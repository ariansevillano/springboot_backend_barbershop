package com.diamondbarbershop.apibarbershop.models;

import com.diamondbarbershop.apibarbershop.util.EstadoReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservas")
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Long reserva_id;
    // Relación con Barbero
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    // Relación con HorarioRango
    @ManyToOne
    @JoinColumn(name = "horarioRango_id", nullable = false)
    private HorarioRango horarioRango;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;
    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private ServicioEntity servicioEntity;
    @Column(nullable = false)
    private Long precioServicio;
    private String motivoDescripcion;
    private String adicionales;
    // Fecha de creación de la reserva
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @Column(nullable = false)
    private LocalDate fechaReserva;
    private Integer estRecompensa;
    private String urlPago;
}
