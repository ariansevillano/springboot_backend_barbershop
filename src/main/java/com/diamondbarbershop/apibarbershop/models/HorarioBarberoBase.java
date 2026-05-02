package com.diamondbarbershop.apibarbershop.models;

import com.diamondbarbershop.apibarbershop.util.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_barbero_base",uniqueConstraints = {@UniqueConstraint(columnNames = {"barbero_id","tipoHorario_id","dia"})})
public class HorarioBarberoBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horario_barbero_base_id")
    private Long horarioBarberoBase_id;
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
    @ManyToOne
    @JoinColumn(name = "tipoHorario_id", nullable = false)
    private TipoHorario tipoHorario;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;
    //hace referencia a activo en el horarioInstancia o descanso, o sea 1 = trabaja en dicho horarioInstancia, null = descansa
    private Integer est_id;
    //hace referencia a trabajador activado, o sea 1 = normal, 0 = apagado o despedido para ya no usarse
    private Integer estado;
}
