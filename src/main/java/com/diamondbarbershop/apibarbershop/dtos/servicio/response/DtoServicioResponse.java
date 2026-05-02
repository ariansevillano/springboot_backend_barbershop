package com.diamondbarbershop.apibarbershop.dtos.servicio.response;

import lombok.Data;

@Data
public class DtoServicioResponse {
    private Long servicio_id;
    private String nombre;
    private Long precio;
    private String descripcion;
    private String nombre_tipoServicio;
    private String urlServicio;
}
