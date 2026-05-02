package com.diamondbarbershop.apibarbershop.services;

import com.diamondbarbershop.apibarbershop.dtos.horarioRangos.response.DtoHorarioRangoResponse;
import com.diamondbarbershop.apibarbershop.exceptions.UsuarioExistenteException;
import com.diamondbarbershop.apibarbershop.models.HorarioRango;
import com.diamondbarbershop.apibarbershop.repositories.IHorarioRangoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioRangoService {

    private final IHorarioRangoRepository horarioRangoRepository;
    public List<DtoHorarioRangoResponse> readAll() {
        List<HorarioRango> horarioRango = horarioRangoRepository.findAll();
        return horarioRango.stream().map(rango -> {
            DtoHorarioRangoResponse dto = new DtoHorarioRangoResponse();
            dto.setHorarioRango_id(rango.getHorarioRango_id());
            dto.setRango(rango.getRango());
            dto.setTipoHorario(rango.getTipoHorario().getNombre());
            return dto;
        }).toList();
    }

    public DtoHorarioRangoResponse readOne(Long id) {
        HorarioRango horarioRango = horarioRangoRepository.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException("No existe el rango"));
        DtoHorarioRangoResponse dto = new DtoHorarioRangoResponse();
        dto.setHorarioRango_id(horarioRango.getHorarioRango_id());
        dto.setRango(horarioRango.getRango());
        dto.setTipoHorario(horarioRango.getTipoHorario().getNombre());
        return dto;
    }
}
