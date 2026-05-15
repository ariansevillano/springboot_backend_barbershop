package com.diamondbarbershop.apibarbershop.services;

import com.diamondbarbershop.apibarbershop.cloudinaryImages.service.CloudinaryService;
import com.diamondbarbershop.apibarbershop.dtos.barbero.response.DtoBarberoDisponible;
import com.diamondbarbershop.apibarbershop.dtos.reserva.request.DtoReserva;
import com.diamondbarbershop.apibarbershop.dtos.reserva.response.DtoReporteResponse;
import com.diamondbarbershop.apibarbershop.dtos.reserva.response.DtoReservaResponse;
import com.diamondbarbershop.apibarbershop.exceptions.BarberoNoEncontradoException;
import com.diamondbarbershop.apibarbershop.exceptions.ServicioNoEncontradoException;
import com.diamondbarbershop.apibarbershop.exceptions.TipoHorarioNoEncotradoException;
import com.diamondbarbershop.apibarbershop.exceptions.UsuarioExistenteException;
import com.diamondbarbershop.apibarbershop.mappers.ReservaEntityMapper;
import com.diamondbarbershop.apibarbershop.models.*;
import com.diamondbarbershop.apibarbershop.repositories.*;
import com.diamondbarbershop.apibarbershop.util.EstadoReserva;
import com.diamondbarbershop.apibarbershop.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final IHorarioBarberoInstanciaRepository horarioBarberoInstanciaRepository;
    private final IHorarioRangoRepository horarioRangoRepository;
    private final IReservaRepository reservaRepository;
    private final IUsuariosRepository usuariosRepository;
    private final IBarberoRepository barberoRepository;
    private final CloudinaryService cloudinaryService;
    private final IServicioRepository servicioRepository;
    private final ReservaEntityMapper reservaEntityMapper;

    public List<DtoBarberoDisponible> listarBarberosDisponibles(LocalDate fecha, Long tipoHorarioId, Long horarioRangoId) {
        // 1. Barberos que trabajan ese día y tipoHorario
        List<HorarioBarberoInstancia> instancias = horarioBarberoInstanciaRepository
                .findByFechaAndTipoHorario_Id(fecha, tipoHorarioId);

        // 2. Reservas existentes para ese día y rango
        HorarioRango horarioRango = horarioRangoRepository.findById(horarioRangoId)
                .orElseThrow(() -> new RuntimeException("HorarioRango no encontrado"));
        List<ReservaEntity> reservaEntities = reservaRepository.findByFechaReservaAndHorarioRango(fecha, horarioRango);

        Set<Long> barberosReservados = reservaEntities.stream()
                .map(r -> r.getBarbero().getBarbero_id())
                .collect(Collectors.toSet());

        // 3. Mapear a DTO con flag disponible
        return instancias.stream()
                .map(instancia -> {
                    Barbero barbero = instancia.getBarbero();
                    DtoBarberoDisponible dto = new DtoBarberoDisponible();
                    dto.setBarberoId(barbero.getBarbero_id());
                    dto.setNombre(barbero.getNombre());
                    dto.setUrlBarbero(barbero.getUrlBarbero());
                    dto.setDisponible(!barberosReservados.contains(barbero.getBarbero_id()));
                    return dto;
                }).toList();
    }

    public Boolean verificarDisponibilidadBarbero(DtoReserva dto, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        Barbero barbero = barberoRepository.findById(dto.getBarberoId())
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));
        HorarioRango horarioRango = horarioRangoRepository.findById(dto.getHorarioRangoId())
                .orElseThrow(() -> new RuntimeException("HorarioRango no encontrado"));
        ServicioEntity servicioEntity = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new RuntimeException("ServicioEntity no encontrado"));

        // Verificar si ya existe reserva para ese barbero, fecha y rango
        boolean existe = reservaRepository.existsByBarberoAndFechaReservaAndHorarioRango(
                barbero, dto.getFechaReserva(), horarioRango);
        if (existe)
            throw new RuntimeException("El barbero ya tiene una reserva en ese horario");
        else
            return true;
    }
    public void crearReserva(DtoReserva dto, Authentication authentication, Boolean flag) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        Barbero barbero = barberoRepository.findById(dto.getBarberoId())
                .orElseThrow(() -> new BarberoNoEncontradoException(MensajeError.BARBERO_NO_ENCONTRADO));
        HorarioRango horarioRango = horarioRangoRepository.findById(dto.getHorarioRangoId())
                .orElseThrow(() -> new TipoHorarioNoEncotradoException(MensajeError.TIPO_HORARIO_NO_ENCOTRADO));
        ServicioEntity servicioEntity = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.SERVICIO_NO_ENCONTRADO));

        // Verificar si ya existe reservaEntity para ese barbero, fecha y rango
        boolean existe = reservaRepository.existsByBarberoAndFechaReservaAndHorarioRango(
                barbero, dto.getFechaReserva(), horarioRango);
        if (existe) throw new RuntimeException("El barbero ya tiene una reservaEntity en ese horario");

        ReservaEntity reservaEntity = new ReservaEntity();
        reservaEntity.setBarbero(barbero);
        reservaEntity.setUsuario(usuario);
        reservaEntity.setHorarioRango(horarioRango);
        reservaEntity.setServicioEntity(servicioEntity);
        reservaEntity.setPrecioServicio(servicioEntity.getPrecio()); // Guardar precio histórico
        reservaEntity.setEstado(EstadoReserva.CREADA);
        reservaEntity.setAdicionales(dto.getAdicionales());
        reservaEntity.setFechaCreacion(LocalDateTime.now());
        reservaEntity.setFechaReserva(dto.getFechaReserva());
        reservaEntity.setUrlPago(null);
        if (flag){
        reservaEntity.setEstRecompensa(1);
            reservaEntity.setEstado(EstadoReserva.CREADA);
        } else {
            reservaEntity.setEstRecompensa(1);
            reservaEntity.setEstado(EstadoReserva.CONFIRMADA);
        }
        reservaRepository.save(reservaEntity);
    }


    public void subirComprobante(Long reservaId, MultipartFile imagen, Authentication authentication) {
        ReservaEntity reservaEntity = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("ReservaEntity no encontrada"));
        String username = authentication.getName();
        if (!reservaEntity.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No autorizado");
        }
        String url = cloudinaryService.subirImagen(imagen, "pagos");
        reservaEntity.setUrlPago(url);
        reservaRepository.save(reservaEntity);
    }


    public List<DtoReservaResponse> listarReservas(LocalDate fecha, EstadoReserva estado, Long usuarioId) {

        List<ReservaEntity> reservaEntities;
        if (usuarioId != null){
            Usuario usuario = usuariosRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
            if (fecha != null && estado != null) {
                reservaEntities = reservaRepository.findByFechaReservaAndEstadoAndUsuario(fecha,estado,usuario);
            } else if (estado != null) {
                reservaEntities = reservaRepository.findByEstadoAndUsuario(estado,usuario);
            } else if (fecha != null){
                reservaEntities = reservaRepository.findByFechaReservaAndUsuario(fecha,usuario);
            } else {
                reservaEntities = reservaRepository.findByUsuario(usuario);
            }
        } else {

            if (fecha != null && estado != null) {
                reservaEntities = reservaRepository.findByFechaReservaAndEstado(fecha,estado);
            } else if (fecha != null) {
                reservaEntities = reservaRepository.findByFechaReserva(fecha);
            } else if (estado != null) {
                reservaEntities = reservaRepository.findByEstado(estado);
            } else {
                reservaEntities = reservaRepository.findAll();
            }
        }
        Long montoTotal = calcularGanancia(reservaEntities);
        List<DtoReservaResponse> dtoReservas = reservaEntityMapper.toDtoList(reservaEntities);
        dtoReservas.forEach(dtoReserva -> dtoReserva.setMontoTotal(montoTotal));
        return dtoReservas;
    }

    public Long calcularGanancia(List<ReservaEntity> reservaEntities) {
        return reservaEntities.stream()
                .filter(e -> e.getEstado() == EstadoReserva.REALIZADA)
                .mapToLong(ReservaEntity::getPrecioServicio)
                .sum();
    }

    public void cambiarEstado(Long reservaId, EstadoReserva estado, String motivoDescripcion) {
        ReservaEntity reservaEntity = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("ReservaEntity no encontrada"));
        if (estado == EstadoReserva.CONFIRMADA) {
            reservaEntity.setEstado(estado);
            reservaEntity.setEstRecompensa(0);
        } else {
            reservaEntity.setEstado(estado);
        }
        // Solo el admin puede poner motivoDescripcion
        if (motivoDescripcion != null) {
            reservaEntity.setMotivoDescripcion(motivoDescripcion);
        }
        reservaRepository.save(reservaEntity);
    }

    public List<DtoReservaResponse> listarReservasPorUsuario(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        List<ReservaEntity> reservaEntities = reservaRepository.findByUsuario(usuario);
        return reservaEntityMapper.toDtoList(reservaEntities);
    }

    public Boolean buscarReservasRecompensa(Authentication authentication) {
        Boolean estado;
        List<ReservaEntity> reservaEntities = filtradoReservasRecompensa(authentication);
        if (reservaEntities.size() >= 7) {
            estado = true;
            return estado;
        } else {
            estado = false;
            return estado;
        }
    }

    public List<ReservaEntity> filtradoReservasRecompensa(Authentication authentication){
        Usuario usuario = usuariosRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        List<ReservaEntity> reservaEntities = reservaRepository.findByUsuario(usuario);
        return reservaEntities = reservaEntities.stream()
                .filter(e -> e.getEstRecompensa() == 0)
                .collect(Collectors.toList());
    }

    @Transactional
    public void crearReservaRecompensa(DtoReserva dto, Authentication authentication) {
        List<ReservaEntity> reservaEntities = filtradoReservasRecompensa(authentication);
        for (ReservaEntity reservaEntity : reservaEntities) {
            reservaEntity.setEstRecompensa(1);
            reservaRepository.save(reservaEntity);
        }
        crearReserva(dto, authentication,false);
    }

    public DtoReporteResponse obtenerReportes(LocalDate fechaInicio, LocalDate fechaFin, String servicio) {
        List<ReservaEntity> reservaEntities = reservaRepository.findByFechaReservaBetweenAndEstado(fechaInicio,fechaFin,EstadoReserva.REALIZADA);
        Long montoTotal = 0L;
        Integer cantidadReservas = 0;
        if (servicio != null) {
            reservaEntities.stream()
                    .filter(e -> e.getServicioEntity().getNombre() == servicio)
                    .collect(Collectors.toList());
           montoTotal = calcularGanancia(reservaEntities);
           cantidadReservas = reservaEntities.size();
           DtoReporteResponse dto = new DtoReporteResponse();
           dto.setServicioNombre(servicio);
           dto.setMontoTotal(montoTotal);
           dto.setCantidadReservas(cantidadReservas);
           return dto;
        } else {
            montoTotal = calcularGanancia(reservaEntities);
            cantidadReservas = reservaEntities.size();
            DtoReporteResponse dto = new DtoReporteResponse();
            dto.setServicioNombre(servicio);
            dto.setMontoTotal(montoTotal);
            dto.setCantidadReservas(cantidadReservas);
            return dto;
        }
    }


}
