package com.diamondbarbershop.apibarbershop.services;

import com.diamondbarbershop.apibarbershop.dtos.valoracion.request.DtoValoracion;
import com.diamondbarbershop.apibarbershop.dtos.valoracion.response.DtoValoracionResponse;
import com.diamondbarbershop.apibarbershop.exceptions.UsuarioExistenteException;
import com.diamondbarbershop.apibarbershop.models.Usuario;
import com.diamondbarbershop.apibarbershop.models.Valoracion;
import com.diamondbarbershop.apibarbershop.repositories.IUsuariosRepository;
import com.diamondbarbershop.apibarbershop.repositories.IValoracionRepository;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValoracionService {

    private final IUsuariosRepository usuariosRepository;
    private final IValoracionRepository valoracionRepository;

    public void crear(DtoValoracion dtoValoracion, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                        .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        Valoracion valoracion = new Valoracion();
        valoracion.setValoracion(dtoValoracion.getValoracion());
        valoracion.setUtil(dtoValoracion.getUtil());
        valoracion.setMensaje(dtoValoracion.getMensaje());
        valoracion.setUsuario(usuario);
        valoracion.setEstado(1);
        valoracionRepository.save(valoracion);
    }

    public List<DtoValoracionResponse> listarValoraciones() {
        List<Valoracion> valoracions = valoracionRepository.findAll();
        return valoracions.stream()
                .filter(e -> e.getEstado() == 1)
                .map(valoracion -> {
            DtoValoracionResponse dto = new DtoValoracionResponse();
            dto.setValoracion_id(valoracion.getValoracion_id());
            dto.setValoracion(valoracion.getValoracion());
            dto.setUtil(valoracion.getUtil());
            dto.setMensaje(valoracion.getMensaje());
            dto.setUsuario_nombre(valoracion.getUsuario().getNombre());
            dto.setUsuarioId(valoracion.getUsuario().getUsuario_id());
            dto.setCelular(valoracion.getUsuario().getCelular());
            return dto;
        }).toList();
    }

    public void cambiarEstado(Long valoracionId){
        Valoracion valoracion = valoracionRepository.findById(valoracionId)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));
        valoracion.setEstado(0);
        valoracionRepository.save(valoracion);
    }
}
