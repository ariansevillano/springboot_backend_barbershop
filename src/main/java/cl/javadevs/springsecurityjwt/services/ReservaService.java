package cl.javadevs.springsecurityjwt.services;

import cl.javadevs.springsecurityjwt.cloudinaryImages.service.CloudinaryService;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoDisponible;
import cl.javadevs.springsecurityjwt.dtos.horarioInstancia.response.DtoHorarioBarberoInstanciaResponse;
import cl.javadevs.springsecurityjwt.dtos.reserva.request.DtoReserva;
import cl.javadevs.springsecurityjwt.dtos.reserva.response.DtoReporteResponse;
import cl.javadevs.springsecurityjwt.dtos.reserva.response.DtoReservaResponse;
import cl.javadevs.springsecurityjwt.exceptions.BarberoNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.exceptions.TipoHorarioNoEncotradoException;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.*;
import cl.javadevs.springsecurityjwt.repositories.*;
import cl.javadevs.springsecurityjwt.util.EstadoReserva;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
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

    public List<DtoBarberoDisponible> listarBarberosDisponibles(LocalDate fecha, Long tipoHorarioId, Long horarioRangoId) {
        // 1. Barberos que trabajan ese día y tipoHorario
        List<HorarioBarberoInstancia> instancias = horarioBarberoInstanciaRepository
                .findByFechaAndTipoHorario_Id(fecha, tipoHorarioId);

        // 2. Reservas existentes para ese día y rango
        HorarioRango horarioRango = horarioRangoRepository.findById(horarioRangoId)
                .orElseThrow(() -> new RuntimeException("HorarioRango no encontrado"));
        List<Reserva> reservas = reservaRepository.findByFechaReservaAndHorarioRango(fecha, horarioRango);

        Set<Long> barberosReservados = reservas.stream()
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
        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

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
        Servicio servicio = servicioRepository.findById(dto.getServicioId())
                .orElseThrow(() -> new ServicioNoEncontradoException(MensajeError.SERVICIO_NO_ENCONTRADO));

        // Verificar si ya existe reserva para ese barbero, fecha y rango
        boolean existe = reservaRepository.existsByBarberoAndFechaReservaAndHorarioRango(
                barbero, dto.getFechaReserva(), horarioRango);
        if (existe) throw new RuntimeException("El barbero ya tiene una reserva en ese horario");

        Reserva reserva = new Reserva();
        reserva.setBarbero(barbero);
        reserva.setUsuario(usuario);
        reserva.setHorarioRango(horarioRango);
        reserva.setServicio(servicio);
        reserva.setPrecioServicio(servicio.getPrecio()); // Guardar precio histórico
        reserva.setEstado(EstadoReserva.CREADA);
        reserva.setAdicionales(dto.getAdicionales());
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setUrlPago(null);
        if (flag){
        reserva.setEstRecompensa(1);
            reserva.setEstado(EstadoReserva.CREADA);
        } else {
            reserva.setEstRecompensa(1);
            reserva.setEstado(EstadoReserva.CONFIRMADA);
        }
        reservaRepository.save(reserva);
    }


    public void subirComprobante(Long reservaId, MultipartFile imagen, Authentication authentication) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        String username = authentication.getName();
        if (!reserva.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No autorizado");
        }
        String url = cloudinaryService.subirImagen(imagen, "pagos");
        reserva.setUrlPago(url);
        reservaRepository.save(reserva);
    }


    public List<DtoReservaResponse> listarReservas(LocalDate fecha, EstadoReserva estado, Long usuarioId) {

        List<Reserva> reservas;
        if (usuarioId != null){
            Usuario usuario = usuariosRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
            if (fecha != null && estado != null) {
                reservas = reservaRepository.findByFechaReservaAndEstadoAndUsuario(fecha,estado,usuario);
            } else if (estado != null) {
                reservas = reservaRepository.findByEstadoAndUsuario(estado,usuario);
            } else if (fecha != null){
                reservas = reservaRepository.findByFechaReservaAndUsuario(fecha,usuario);
            } else {
                reservas = reservaRepository.findByUsuario(usuario);
            }
        } else {

            if (fecha != null && estado != null) {
                reservas = reservaRepository.findByFechaReservaAndEstado(fecha,estado);
            } else if (fecha != null) {
                reservas = reservaRepository.findByFechaReserva(fecha);
            } else if (estado != null) {
                reservas = reservaRepository.findByEstado(estado);
            } else {
                reservas = reservaRepository.findAll();
            }
        }
        Long montoTotal = calcularGanancia(reservas);
        return reservas.stream().map(reserva -> {
            DtoReservaResponse dto = new DtoReservaResponse();
            dto.setReservaId(reserva.getReserva_id());
            dto.setBarberoNombre(reserva.getBarbero().getNombre());
            dto.setUsuarioId(reserva.getUsuario().getUsuario_id());
            dto.setUsuarioNombre(reserva.getUsuario().getNombre());
            dto.setHorarioRango(reserva.getHorarioRango().getRango());
            dto.setEstado(reserva.getEstado().name());
            dto.setMotivoDescripcion(reserva.getMotivoDescripcion());
            dto.setAdicionales(reserva.getAdicionales());
            dto.setFechaCreacion(reserva.getFechaCreacion());
            dto.setFechaReserva(reserva.getFechaReserva());
            dto.setUrlPago(reserva.getUrlPago());
            dto.setServicioNombre(reserva.getServicio().getNombre());
            dto.setPrecioServicio(reserva.getPrecioServicio());
            dto.setEstRecompensa(reserva.getEstRecompensa());
            dto.setMontoTotal(montoTotal);
            return dto;
        }).toList();
    }

    public Long calcularGanancia(List<Reserva> reservas) {
        return reservas.stream()
                .filter(e -> e.getEstado() == EstadoReserva.REALIZADA)
                .mapToLong(Reserva::getPrecioServicio)
                .sum();
    }

    public void cambiarEstado(Long reservaId, EstadoReserva estado, String motivoDescripcion) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        if (estado == EstadoReserva.CONFIRMADA) {
            reserva.setEstado(estado);
            reserva.setEstRecompensa(0);
        } else {
            reserva.setEstado(estado);
        }
        // Solo el admin puede poner motivoDescripcion
        if (motivoDescripcion != null) {
            reserva.setMotivoDescripcion(motivoDescripcion);
        }
        reservaRepository.save(reserva);
    }

    public List<DtoReservaResponse> listarReservasPorUsuario(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuariosRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        List<Reserva> reservas = reservaRepository.findByUsuario(usuario);
        return reservas.stream().map(reserva -> {
            DtoReservaResponse dto = new DtoReservaResponse();
            dto.setReservaId(reserva.getReserva_id());
            dto.setBarberoNombre(reserva.getBarbero().getNombre());
            dto.setUsuarioNombre(reserva.getUsuario().getNombre());
            dto.setHorarioRango(reserva.getHorarioRango().getRango());
            dto.setEstado(reserva.getEstado().name());
            dto.setMotivoDescripcion(reserva.getMotivoDescripcion());
            dto.setAdicionales(reserva.getAdicionales());
            dto.setFechaCreacion(reserva.getFechaCreacion());
            dto.setFechaReserva(reserva.getFechaReserva());
            dto.setUrlPago(reserva.getUrlPago());
            dto.setServicioNombre(reserva.getServicio().getNombre());
            dto.setEstRecompensa(reserva.getEstRecompensa());
            dto.setPrecioServicio(reserva.getPrecioServicio());
            return dto;
        }).toList();
    }

    public Boolean buscarReservasRecompensa(Authentication authentication) {
        Boolean estado;
        List<Reserva> reservas = filtradoReservasRecompensa(authentication);
        if (reservas.size() >= 7) {
            estado = true;
            return estado;
        } else {
            estado = false;
            return estado;
        }
    }

    public List<Reserva> filtradoReservasRecompensa(Authentication authentication){
        Usuario usuario = usuariosRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));
        List<Reserva> reservas = reservaRepository.findByUsuario(usuario);
        return reservas = reservas.stream()
                .filter(e -> e.getEstRecompensa() == 0)
                .collect(Collectors.toList());
    }

    @Transactional
    public void crearReservaRecompensa(DtoReserva dto, Authentication authentication) {
        List<Reserva> reservas = filtradoReservasRecompensa(authentication);
        for (Reserva reserva : reservas ) {
            reserva.setEstRecompensa(1);
            reservaRepository.save(reserva);
        }
        crearReserva(dto, authentication,false);
    }

    public DtoReporteResponse obtenerReportes(LocalDate fechaInicio, LocalDate fechaFin, String servicio) {
        List<Reserva> reservas = reservaRepository.findByFechaReservaBetweenAndEstado(fechaInicio,fechaFin,EstadoReserva.REALIZADA);
        Long montoTotal = 0L;
        Integer cantidadReservas = 0;
        if (servicio != null) {
            reservas.stream()
                    .filter(e -> e.getServicio().getNombre() == servicio)
                    .collect(Collectors.toList());
           montoTotal = calcularGanancia(reservas);
           cantidadReservas = reservas.size();
           DtoReporteResponse dto = new DtoReporteResponse();
           dto.setServicioNombre(servicio);
           dto.setMontoTotal(montoTotal);
           dto.setCantidadReservas(cantidadReservas);
           return dto;
        } else {
            montoTotal = calcularGanancia(reservas);
            cantidadReservas = reservas.size();
            DtoReporteResponse dto = new DtoReporteResponse();
            dto.setServicioNombre(servicio);
            dto.setMontoTotal(montoTotal);
            dto.setCantidadReservas(cantidadReservas);
            return dto;
        }
    }


}
