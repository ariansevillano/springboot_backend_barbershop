package com.diamondbarbershop.apibarbershop.mappers;

import com.diamondbarbershop.apibarbershop.dtos.servicio.response.DtoServicioResponse;
import com.diamondbarbershop.apibarbershop.models.ServicioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Data Mapper — transforma ServicioEntity (JPA) en DtoServicioResponse.
 *
 * El único campo no trivial es nombre_tipoServicio: viene de un objeto
 * relacionado (tipoServicio.nombre), no de un campo directo.
 */
@Mapper(componentModel = "spring")
public interface ServicioEntityMapper {

    @Mapping(source = "tipoServicio.nombre", target = "nombre_tipoServicio")
    DtoServicioResponse toDto(ServicioEntity servicioEntity);

    List<DtoServicioResponse> toDtoList(List<ServicioEntity> servicioEntities);
}
