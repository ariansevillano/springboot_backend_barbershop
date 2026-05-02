package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.cloudinaryImages.service.CloudinaryService;
import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.dtos.servicio.response.DtoServicioResponse;
import cl.javadevs.springsecurityjwt.exceptions.BarberoNoEncontradoException;
import cl.javadevs.springsecurityjwt.mappers.BarberoMapper;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.repositories.IBarberoRepository;
import cl.javadevs.springsecurityjwt.repositories.IHorarioBarberoBaseRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarberoService {

    private final IBarberoRepository barberoRepository;
    private final AuthenticationManager authenticationManager;
    private final HorarioBarberoBaseService horarioBarberoBaseService;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public void crear(DtoBarbero dtoBarbero, MultipartFile imagen) {
        String urlImagen = null;

        if (imagen != null) {
            urlImagen = cloudinaryService.subirImagen(imagen, "barberos");
        }
        Barbero barbero = new Barbero();
        barbero.setNombre(dtoBarbero.getNombre());
        barbero.setEstado(1);
        if (urlImagen != null) {
            barbero.setUrlBarbero(urlImagen);
        }
        barberoRepository.save(barbero);
        horarioBarberoBaseService.crearHorarioBaseInicial(barbero.getBarbero_id());
    }

    public List<DtoBarberoResponse> readAll(){
        List<Barbero> barberos = barberoRepository.findAll();
        return barberos.stream().filter(barbero ->
                        barbero.getEstado() == 1)
                .map(barbero -> {
                    DtoBarberoResponse dto = BarberoMapper.toDto(barbero);
                    return dto;
                }).toList();
    }

    public void deshabilitar(Long id) {
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new BarberoNoEncontradoException("Barbero no encontrado con el id: " + id));
        barbero.setEstado(0);
        barberoRepository.save(barbero);
    }

    public void update(Long id,DtoBarbero dtoBarbero,MultipartFile imagen){
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new BarberoNoEncontradoException("Barbero no encontrada con id: " + id));

        String urlImagen = null;

        if (imagen != null) {
            urlImagen = cloudinaryService.subirImagen(imagen, "barberos");
            barbero.setUrlBarbero(urlImagen);
        }
        barbero.setNombre(dtoBarbero.getNombre());
        barberoRepository.save(barbero);
    }

    public DtoBarberoResponse readOne(Long id){
        Barbero barbero = barberoRepository.findById(id)
                .orElseThrow(() -> new BarberoNoEncontradoException(MensajeError.BARBERO_NO_ENCONTRADO));
        DtoBarberoResponse dto = BarberoMapper.toDto(barbero);
        return dto;
    }
}
