package com.diamondbarbershop.apibarbershop.mappers;

import com.diamondbarbershop.apibarbershop.dtos.valoracion.response.DtoValoracionResponse;
import com.diamondbarbershop.apibarbershop.models.ValoracionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Data Mapper — transforma Valoracion (JPA) en DtoValoracionResponse.
 *
 * Tres campos vienen del objeto usuario relacionado:
 * nombre, id y celular.
 */
@Mapper(componentModel = "spring")
public interface ValoracionEntityMapper {

    @Mapping(source = "usuario.nombre",      target = "usuario_nombre")
    @Mapping(source = "usuario.usuario_id",  target = "usuarioId")
    @Mapping(source = "usuario.celular",     target = "celular")
    DtoValoracionResponse toDto(ValoracionEntity valoracion);

    List<DtoValoracionResponse> toDtoList(List<ValoracionEntity> valoraciones);
}
