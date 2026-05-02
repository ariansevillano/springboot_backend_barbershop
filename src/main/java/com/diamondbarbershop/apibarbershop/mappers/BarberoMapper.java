package com.diamondbarbershop.apibarbershop.mappers;

import com.diamondbarbershop.apibarbershop.dtos.barbero.request.DtoBarbero;
import com.diamondbarbershop.apibarbershop.dtos.barbero.response.DtoBarberoResponse;
import com.diamondbarbershop.apibarbershop.models.Barbero;

public class BarberoMapper {

    public static Barbero toEntity(DtoBarbero dto){
        Barbero barbero = new Barbero();
        barbero.setNombre(dto.getNombre());
        return barbero;
    }
    public static DtoBarberoResponse toDto(Barbero barbero){
        DtoBarberoResponse dto = new DtoBarberoResponse();
        dto.setBarbero_id(barbero.getBarbero_id());
        dto.setNombre(barbero.getNombre());
        dto.setUrlBarbero(barbero.getUrlBarbero());
        return dto;
    }
}
