package com.diamondbarbershop.apibarbershop.controllers;

import com.diamondbarbershop.apibarbershop.dtos.common.ApiResponse;
import com.diamondbarbershop.apibarbershop.dtos.horarioInstancia.response.DtoHorarioBarberoInstanciaResponse;
import com.diamondbarbershop.apibarbershop.services.HorarioBarberoInstanciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horarioInstancia/")
@RequiredArgsConstructor
public class RestControllerHorarioBarberoInstancia {

    private final HorarioBarberoInstanciaService horarioBarberoInstanciaService;

    @GetMapping("/actual")
    public ResponseEntity<ApiResponse<Map<String, List<DtoHorarioBarberoInstanciaResponse>>>> obtenerSemanaAgrupada(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        return ResponseEntity.ok(ApiResponse.succes("Horario Actual:", horarioBarberoInstanciaService.obtenerInstanciasAgrupadasPorDiaSemanaActual()));
    }
}
