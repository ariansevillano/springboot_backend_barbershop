package com.diamondbarbershop.apibarbershop.agenda.domain.model;

import com.diamondbarbershop.apibarbershop.util.DiaSemana;

import java.time.LocalDate;

/**
 * Entity del Bounded Context "Agenda".
 * Representa la entrada concreta de trabajo de un barbero para una fecha específica.
 *
 * Diferencia con HorarioBarberoBase:
 *   - HorarioBarberoBase  → plantilla SEMANAL recurrente ("los lunes trabajo turno mañana")
 *   - HorarioBarberoInstancia → entrada para una FECHA CONCRETA ("el 2026-05-11 trabajo turno mañana")
 *
 * Las instancias se generan automáticamente desde el base mediante el HorarioBaseScheduler.
 *
 * NOTA: Sin anotaciones JPA — esta es la clase de dominio puro.
 */
public class HorarioBarberoInstancia {

    private Long id;
    private Long barberoId;
    private Long tipoHorarioId;
    private DiaSemana dia;
    private LocalDate fecha;

    /**
     * Estado del turno para esta instancia específica.
     * 1 = barbero trabaja en este turno ese día.
     * null = barbero descansa (excepción al horario base, ej. vacaciones o feriado).
     */
    private Integer estId;

    public HorarioBarberoInstancia() {}

    /**
     * Regla de dominio: un barbero "está trabajando" cuando estId es 1.
     * Expresamos esto con un método de dominio en lugar de exponer el campo.
     */
    public boolean estaTrabajando() {
        return this.estId != null && this.estId == 1;
    }

    public Long getId()             { return id; }
    public Long getBarberoId()      { return barberoId; }
    public Long getTipoHorarioId()  { return tipoHorarioId; }
    public DiaSemana getDia()       { return dia; }
    public LocalDate getFecha()     { return fecha; }
    public Integer getEstId()       { return estId; }

    public void setId(Long id)                        { this.id = id; }
    public void setBarberoId(Long barberoId)          { this.barberoId = barberoId; }
    public void setTipoHorarioId(Long tipoHorarioId) { this.tipoHorarioId = tipoHorarioId; }
    public void setDia(DiaSemana dia)                 { this.dia = dia; }
    public void setFecha(LocalDate fecha)             { this.fecha = fecha; }
    public void setEstId(Integer estId)               { this.estId = estId; }
}
