package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.horarioBase.DtoHorarioBase;
import cl.javadevs.springsecurityjwt.services.HorarioBarberoBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarioBarberoBase/")
@RequiredArgsConstructor
public class RestControllerHorarioBarberoBase {
    private final HorarioBarberoBaseService horarioBarberoBaseService;


    @PutMapping("actualizarTurnosDia")
    public ResponseEntity<ApiResponse<Object>> actualizarTurnosDia(@RequestBody DtoHorarioBase dtoHorarioBase, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        horarioBarberoBaseService.actualizarTurnosDia(dtoHorarioBase);
        return ResponseEntity.ok(ApiResponse.succes("Turnos actualizados correctamente",null));
    }

    @PutMapping("confirmarHorario")
    public ResponseEntity<ApiResponse<Object>> confirmarHorario(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        horarioBarberoBaseService.confirmarHorarioBaseParaSemanasSiguientes();
        return ResponseEntity.ok(ApiResponse.succes("Horario confirmado para la próxima semana",null));
    }
}
