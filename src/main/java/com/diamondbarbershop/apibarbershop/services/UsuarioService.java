package com.diamondbarbershop.apibarbershop.services;

import com.diamondbarbershop.apibarbershop.cloudinaryImages.service.CloudinaryService;
import com.diamondbarbershop.apibarbershop.dtos.usuario.request.DtoUsuario;
import com.diamondbarbershop.apibarbershop.dtos.usuario.response.DtoUsuarioResponse;
import com.diamondbarbershop.apibarbershop.exceptions.UsuarioExistenteException;
import com.diamondbarbershop.apibarbershop.mappers.UsuarioMapper;
import com.diamondbarbershop.apibarbershop.models.Usuario;
import com.diamondbarbershop.apibarbershop.repositories.IUsuariosRepository;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final IUsuariosRepository usuariosRepository;
    private final CloudinaryService cloudinaryService;

    public DtoUsuarioResponse readOne(Long id){
        Usuario usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        DtoUsuarioResponse dto = UsuarioMapper.toDto(usuario);
        return dto;
    }

    public DtoUsuarioResponse readOneByAuth(Authentication authentication){
        String name = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(name)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        DtoUsuarioResponse dto = UsuarioMapper.toDto(usuario);
        return dto;
    }

    public List<DtoUsuarioResponse> readAll() {
        List<Usuario> usuarios = usuariosRepository.findAll();
        return usuarios.stream().filter(usuario -> usuario.getRoles().
                        stream().anyMatch(rol -> "USER".equals(rol.getName())))
                .map(usuario -> {
            DtoUsuarioResponse dto = UsuarioMapper.toDto(usuario);
            return dto;
        }).toList();
    }

    public void update(Long id, DtoUsuario dtoUsuario, MultipartFile imagen) {
        String urlImagen = null;

        Usuario usuario = usuariosRepository.findById(id)
                .orElseThrow(()-> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));

        if (imagen != null){
            urlImagen = cloudinaryService.subirImagen(imagen,"usuarios");
            usuario.setUrlUsuario(urlImagen);
        }
        usuario.setNombre(dtoUsuario.getNombre());
        usuario.setApellido(dtoUsuario.getApellido());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setCelular(dtoUsuario.getCelular());
        usuariosRepository.save(usuario);
    }

    public void updateByAuth(DtoUsuario dtoUsuario, MultipartFile imagen, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));

        if (imagen != null) {
            String urlImagen = cloudinaryService.subirImagen(imagen, "usuarios");
            usuario.setUrlUsuario(urlImagen);
        }
        usuario.setNombre(dtoUsuario.getNombre());
        usuario.setApellido(dtoUsuario.getApellido());
        usuario.setEmail(dtoUsuario.getEmail());
        usuario.setCelular(dtoUsuario.getCelular());
        usuariosRepository.save(usuario);
    }

}
