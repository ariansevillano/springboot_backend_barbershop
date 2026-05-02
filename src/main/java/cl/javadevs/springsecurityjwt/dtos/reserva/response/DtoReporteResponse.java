package cl.javadevs.springsecurityjwt.dtos.reserva.response;

import lombok.Data;

@Data
public class DtoReporteResponse {

    private String servicioNombre;
    private Long montoTotal;
    private Integer cantidadReservas;
}
