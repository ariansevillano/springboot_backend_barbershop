package com.diamondbarbershop.apibarbershop.dtos.barbero.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class DtoBarberoResponse {
    private Long barbero_id;
    private String nombre;
    private String urlBarbero;
}
