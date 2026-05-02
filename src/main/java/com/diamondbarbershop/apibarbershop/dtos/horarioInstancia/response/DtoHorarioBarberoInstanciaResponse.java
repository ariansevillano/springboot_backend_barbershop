package com.diamondbarbershop.apibarbershop.dtos.horarioInstancia.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DtoHorarioBarberoInstanciaResponse {
    private LocalDate fecha;
    private String dia;
    private String tipoHorario;
    private String barbero;
}
