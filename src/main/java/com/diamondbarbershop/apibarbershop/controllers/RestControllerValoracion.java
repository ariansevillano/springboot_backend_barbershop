package com.diamondbarbershop.apibarbershop.controllers;

import com.diamondbarbershop.apibarbershop.dtos.common.ApiResponse;
import com.diamondbarbershop.apibarbershop.dtos.valoracion.request.DtoValoracion;
import com.diamondbarbershop.apibarbershop.dtos.valoracion.response.DtoValoracionResponse;
import com.diamondbarbershop.apibarbershop.services.ValoracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valoracion/")
@RequiredArgsConstructor
public class RestControllerValoracion {

    private final ValoracionService valoracionService;

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearValoracion(@RequestBody @Valid DtoValoracion dtoValoracion, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }

        valoracionService.crear(dtoValoracion,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("ServicioEntity creado correctamente", null)
        );
    }

    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoValoracionResponse>>> listarValoracion(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoValoracionResponse> valoraciones = valoracionService.listarValoraciones();
        return ResponseEntity.ok(ApiResponse.succes("Lista de valoraciones obtenida correctamente",valoraciones));
    }

    @GetMapping(value = "responder/{valoracionId}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> cambiarEstado(@PathVariable Long valoracionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        valoracionService.cambiarEstado(valoracionId);
        return ResponseEntity.ok(ApiResponse.succes("Valoración respondida", null));
    }
}
