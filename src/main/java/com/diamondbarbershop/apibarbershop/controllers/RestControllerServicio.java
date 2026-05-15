package com.diamondbarbershop.apibarbershop.controllers;

import com.diamondbarbershop.apibarbershop.dtos.common.ApiResponse;
import com.diamondbarbershop.apibarbershop.dtos.servicio.request.DtoServicio;
import com.diamondbarbershop.apibarbershop.dtos.servicio.response.DtoServicioResponse;
import com.diamondbarbershop.apibarbershop.exceptions.ImagenNoSubidaException;
import com.diamondbarbershop.apibarbershop.services.ServicioService;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/servicio/")
@RequiredArgsConstructor
public class RestControllerServicio {

    private final ServicioService servicioService;

    //Petición para crear un  servicio
    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearServicio(@RequestPart("dtoServicio") @Valid DtoServicio dtoServicio,
                                @RequestPart("imagen") MultipartFile imagen) {
        if (imagen.getContentType() == null || !imagen.getContentType().startsWith("image/")) {
            throw new ImagenNoSubidaException(MensajeError.TIPO_ARCHIVO_NO_PERMITIDO);
        }
        servicioService.crear(dtoServicio,imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("ServicioEntity creado correctamente", null)
        );
    }

    //Petición para obtener todos los servicio en la BD
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoServicioResponse>>> listarServicio() {
        List<DtoServicioResponse> dtoServicios = servicioService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de servicios obtenida correctamente",dtoServicios));
    }

    //Petición para obtener servicio mediante "ID"
    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoServicioResponse>> obtenerServicioPorId(@PathVariable Long id) {
        DtoServicioResponse dtoServicio = servicioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("ServicioEntity encontrado",dtoServicio));
    }

    //Petición para actualizar un servicio
    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarServicio(@PathVariable Long id ,@RequestPart DtoServicio dtoServicio,
                                                                  @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        if (imagen != null &&
                (imagen.getContentType() == null
                        || !imagen.getContentType().startsWith("image/"))) {
            throw new ImagenNoSubidaException(MensajeError.TIPO_ARCHIVO_NO_PERMITIDO);
        }
        servicioService.update(id,dtoServicio,imagen);
        DtoServicioResponse dtoServicioResponse = servicioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("ServicioEntity Actualizado exitosamente",dtoServicioResponse));
    }

    //Petición para eliminar un servicio por "Id"
    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> eliminarServicio(@PathVariable Long id) {
        servicioService.deshabilitar(id);
        return ResponseEntity.ok(ApiResponse.succes("ServicioEntity Eliminado exitosamente",null));
    }
}
