package com.diamondbarbershop.apibarbershop.controllers;
import com.diamondbarbershop.apibarbershop.dtos.common.ApiResponse;
import com.diamondbarbershop.apibarbershop.dtos.usuario.request.DtoUsuario;
import com.diamondbarbershop.apibarbershop.dtos.usuario.response.DtoUsuarioResponse;
import com.diamondbarbershop.apibarbershop.exceptions.ImagenNoSubidaException;
import com.diamondbarbershop.apibarbershop.services.UsuarioService;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuario/")
@RequiredArgsConstructor
public class RestControllerUsuario {
    private final UsuarioService usuarioService;
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoUsuarioResponse>>> listarUsuarios() {
        List<DtoUsuarioResponse> usuarios = usuarioService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de usuarios obtenida correctamente",usuarios));
    }

    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoUsuarioResponse>> obtenerUsuarioPorId(@PathVariable Long id) {
        DtoUsuarioResponse usuario = usuarioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Usuario encontrado",usuario));
    }

    @GetMapping(value = "listarme", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoUsuarioResponse>> obtenerMiUsuario(Authentication authentication) {
        DtoUsuarioResponse usuario = usuarioService.readOneByAuth(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Usuario encontrado",usuario));
    }

    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarUsuario(@PathVariable Long id,@RequestPart @Valid DtoUsuario dtoUsuario,
                                                                 @RequestPart (value = "imagen", required = false) MultipartFile imagen) {
        if (imagen != null &&
                (imagen.getContentType() == null
                        || !imagen.getContentType().startsWith("image/"))) {
            throw new ImagenNoSubidaException(MensajeError.TIPO_ARCHIVO_NO_PERMITIDO);
        }
        usuarioService.update(id,dtoUsuario,imagen);
        DtoUsuarioResponse dtoResponse = usuarioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Usuario Actualizado exitosamente",dtoResponse));
    }

    @PutMapping(value = "actualizar-mi-perfil", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarMiPerfil(
            @RequestPart @Valid DtoUsuario dtoUsuario,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            Authentication authentication) {
        usuarioService.updateByAuth(dtoUsuario, imagen, authentication);
        DtoUsuarioResponse dtoResponse = usuarioService.readOneByAuth(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Usuario actualizado exitosamente", dtoResponse));
    }


}
