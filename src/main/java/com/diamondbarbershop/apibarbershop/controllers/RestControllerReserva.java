package com.diamondbarbershop.apibarbershop.controllers;

import com.diamondbarbershop.apibarbershop.dtos.barbero.response.DtoBarberoDisponible;
import com.diamondbarbershop.apibarbershop.dtos.common.ApiResponse;
import com.diamondbarbershop.apibarbershop.dtos.reserva.request.DtoReserva;
import com.diamondbarbershop.apibarbershop.dtos.reserva.response.DtoReporteResponse;
import com.diamondbarbershop.apibarbershop.dtos.reserva.response.DtoReservaResponse;
import com.diamondbarbershop.apibarbershop.services.ReservaService;
import com.diamondbarbershop.apibarbershop.util.EstadoReserva;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/reserva/")
@RequiredArgsConstructor
public class RestControllerReserva {

    private final ReservaService reservaService;
    @GetMapping("barberos-disponibles")
    public ResponseEntity<ApiResponse<List<DtoBarberoDisponible>>> listarBarberosDisponibles(
            Authentication authentication,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("tipoHorarioId") Long tipoHorarioId,
            @RequestParam("horarioRangoId") Long horarioRangoId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoBarberoDisponible> barberos = reservaService.listarBarberosDisponibles(fecha, tipoHorarioId, horarioRangoId);
        return ResponseEntity.ok(ApiResponse.succes("Lista de barberos disponibles",barberos));
    }

    @PostMapping("crear")
    public ResponseEntity<ApiResponse<Object>> crearReserva(
            @RequestBody DtoReserva dto,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        reservaService.crearReserva(dto, authentication, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes("Reserva creada correctamente", null));
    }

    @PostMapping("subir-comprobante/{reservaId}")
    public ResponseEntity<ApiResponse<Object>> subirComprobante(
            @PathVariable Long reservaId,
            @RequestPart("imagen") MultipartFile imagen,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        reservaService.subirComprobante(reservaId, imagen, authentication);
        return ResponseEntity.ok(ApiResponse.succes("Comprobante subido", null));
    }

    @GetMapping("admin/listar")
    public ResponseEntity<ApiResponse<List<DtoReservaResponse>>> listarReservas(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) Long usuarioId ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoReservaResponse> reservas = reservaService.listarReservas(fecha, estado,usuarioId);
        return ResponseEntity.ok(ApiResponse.succes("Lista de reservas",reservas));
    }


    @PutMapping("admin/cambiar-estado/{reservaId}")
    public ResponseEntity<ApiResponse<Object>> cambiarEstado(
            Authentication authentication,
            @PathVariable Long reservaId,
            @RequestParam("estado") EstadoReserva estado,
            @RequestParam(value = "motivoDescripcion", required = false) String motivoDescripcion) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        reservaService.cambiarEstado(reservaId, estado, motivoDescripcion);
        return ResponseEntity.ok(ApiResponse.succes("Estado actualizado", null));
    }


    @GetMapping("mis-reservas")
    public ResponseEntity<ApiResponse<List<DtoReservaResponse>>> listarMisReservas(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoReservaResponse> reservas = reservaService.listarReservasPorUsuario(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Lista de mis reservas",reservas));
    }

    @GetMapping("consultarRecompensa")
    public ResponseEntity<ApiResponse<Boolean>> consultarRecompensa(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        Boolean estado = reservaService.buscarReservasRecompensa(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Estado enviado",estado));
    }

    @PostMapping("crearReservaRecompensa")
    public ResponseEntity<ApiResponse<Object>> crearReservaRecompensa(
            @RequestBody DtoReserva dto,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        reservaService.crearReservaRecompensa(dto,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes("Reserva creada correctamente", null));
    }

    @GetMapping(value = "obtenerReportes", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoReporteResponse>> obtenerReportes(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin,
            @RequestParam (required = false) String servicio,
            Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        DtoReporteResponse reporte = reservaService.obtenerReportes(fechaInicio,fechaFin,servicio);
        return ResponseEntity.ok(ApiResponse.succes("Reportes obtenidos", reporte));
    }
}
