package com.diamondbarbershop.apibarbershop.dtos.barbero.response;

import lombok.Data;

@Data
public class DtoBarberoDisponible {
    private Long barberoId;
    private String nombre;
    private String urlBarbero;
    private boolean disponible;
}
