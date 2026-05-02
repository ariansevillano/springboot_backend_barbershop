package com.diamondbarbershop.apibarbershop.dtos.reserva.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DtoReserva {
    private Long barberoId;
    private Long horarioRangoId;
    private LocalDate fechaReserva;
    private Long servicioId;
    private String adicionales; // Comentarios del usuario
}
