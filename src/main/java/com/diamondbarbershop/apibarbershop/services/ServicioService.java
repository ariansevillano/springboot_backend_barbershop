package com.diamondbarbershop.apibarbershop.services;

import com.diamondbarbershop.apibarbershop.cloudinaryImages.service.CloudinaryService;
import com.diamondbarbershop.apibarbershop.dtos.servicio.request.DtoServicio;
import com.diamondbarbershop.apibarbershop.dtos.servicio.response.DtoServicioResponse;
import com.diamondbarbershop.apibarbershop.exceptions.ServicioNoEncontradoException;
import com.diamondbarbershop.apibarbershop.exceptions.UsuarioExistenteException;
import com.diamondbarbershop.apibarbershop.models.Servicio;
import com.diamondbarbershop.apibarbershop.models.TipoServicio;
import com.diamondbarbershop.apibarbershop.repositories.IServicioRepository;
import com.diamondbarbershop.apibarbershop.repositories.ITipoServicioRepository;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {
    private final IServicioRepository servicioRepo;
    private final ITipoServicioRepository tipoServicioRepository;
    private final CloudinaryService cloudinaryService;

    public void crear(DtoServicio dtoServicio, MultipartFile imagen) {
        String urlImagen = cloudinaryService.subirImagen(imagen,"servicios");

        Servicio servicio = new Servicio();
        servicio.setNombre(dtoServicio.getNombre());
        servicio.setPrecio(dtoServicio.getPrecio());
        servicio.setDescripcion(dtoServicio.getDescripcion());
        servicio.setEstado(1);
        TipoServicio tipoServicio = tipoServicioRepository.findById(dtoServicio.getTipoServicio_id())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.TIPO_SERVICIO_NO_ENCONTRADO));
        servicio.setTipoServicio(tipoServicio);
        servicio.setUrlServicio(urlImagen);
        servicioRepo.save(servicio);
    }


    /*
    public void crear(DtoServicio dtoServicio) {
        Servicio servicio = new Servicio();
        servicio.setNombre(dtoServicio.getNombre());
        servicio.setPrecio(dtoServicio.getPrecio());
        servicio.setDescripcion(dtoServicio.getDescripcion());
        TipoServicio tipoServicio = tipoServicioRepository.findById(dtoServicio.getTipoServicio_id())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.TIPO_SERVICIO_NO_ENCONTRADO));
        servicio.setTipoServicio(tipoServicio);
        servicioRepo.save(servicio);
    }*/

    public List<DtoServicioResponse> readAll() {
        List<Servicio> servicios = servicioRepo.findAll();
        return servicios.stream().filter(servicio ->
                servicio.getEstado() == 1)
                .map(servicio -> {
                    DtoServicioResponse dto = new DtoServicioResponse();
                    dto.setServicio_id(servicio.getServicio_id());
                    dto.setNombre(servicio.getNombre());
                    dto.setPrecio(servicio.getPrecio());
                    dto.setDescripcion(servicio.getDescripcion());
                    dto.setNombre_tipoServicio(servicio.getTipoServicio().getNombre());
                    dto.setUrlServicio(servicio.getUrlServicio());
                    return dto;
        }).toList();
    }

    public DtoServicioResponse readOne(Long id) {
        Servicio servicio = servicioRepo.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        DtoServicioResponse dto = new DtoServicioResponse();
        dto.setServicio_id(servicio.getServicio_id());
        dto.setNombre(servicio.getNombre());
        dto.setPrecio(servicio.getPrecio());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setNombre_tipoServicio(servicio.getTipoServicio().getNombre());
        dto.setUrlServicio(servicio.getUrlServicio());
        return dto;
    }

    public void update(Long id, DtoServicio dtoServicio, MultipartFile imagen) {
        String urlImagen = null;

        Servicio servicio = servicioRepo.findById(id)
                        .orElseThrow(()-> new ServicioNoEncontradoException(MensajeError.SERVICIO_NO_ENCONTRADO));

        if (imagen != null){
            urlImagen = cloudinaryService.subirImagen(imagen,"servicios");
            servicio.setUrlServicio(urlImagen);
        }
        servicio.setNombre(dtoServicio.getNombre());
        servicio.setPrecio(dtoServicio.getPrecio());
        servicio.setDescripcion(dtoServicio.getDescripcion());
        TipoServicio tipoServicio = tipoServicioRepository.findById(dtoServicio.getTipoServicio_id())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.TIPO_SERVICIO_NO_ENCONTRADO));
        servicio.setTipoServicio(tipoServicio);
        servicioRepo.save(servicio);
    }

    public void deshabilitar(Long id) {
        Servicio servicio = servicioRepo.findById(id)
                .orElseThrow(() -> new ServicioNoEncontradoException("No se puede eliminar. Servicio no encontrado con Id: " + id));
        servicio.setEstado(0);
        servicioRepo.save(servicio);
    }
}
