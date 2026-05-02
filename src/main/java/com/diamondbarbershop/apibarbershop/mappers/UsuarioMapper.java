package com.diamondbarbershop.apibarbershop.mappers;

import com.diamondbarbershop.apibarbershop.dtos.auth.request.DtoRegistro;
import com.diamondbarbershop.apibarbershop.dtos.usuario.response.DtoUsuarioResponse;
import com.diamondbarbershop.apibarbershop.models.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(DtoRegistro dtoRegistro){
        Usuario usuario = new Usuario();
        usuario.setUsername(dtoRegistro.getUsername());
        usuario.setPassword(dtoRegistro.getPassword());
        usuario.setNombre(dtoRegistro.getNombre());
        usuario.setApellido(dtoRegistro.getApellido());
        usuario.setEmail(dtoRegistro.getEmail());
        usuario.setCelular(dtoRegistro.getCelular());
        return usuario;
    }

    public static DtoUsuarioResponse toDto(Usuario usuario){
        DtoUsuarioResponse dto = new DtoUsuarioResponse();
        dto.setUsuario_id(usuario.getUsuario_id());
        dto.setUsername(usuario.getUsername());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setCelular(usuario.getCelular());
        dto.setUrlUsuario(usuario.getUrlUsuario());
        return dto;
    }
}
