package com.diamondbarbershop.apibarbershop.dtos.barbero.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DtoBarbero {
    @NotBlank(message = "El campo nombre no puede estar vacío.")
    private String nombre;
}
