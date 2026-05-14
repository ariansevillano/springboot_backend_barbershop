package com.diamondbarbershop.apibarbershop.catalogo.domain.model;

/**
 * Modelo de dominio del Bounded Context "Catálogo".
 *
 * Catálogo gestiona los servicios que ofrece la barbería y sus precios.
 * Es un Supporting Domain — apoya al Core Domain (Reservas) proveyendo
 * información de servicios disponibles.
 *
 * Reglas de negocio:
 *   1. Un servicio nuevo siempre inicia activo.
 *   2. Los servicios se deshabilitan, no se eliminan (integridad histórica
 *      de reservas que ya referenciaron ese servicio).
 *   3. La imagen solo se actualiza si se provee una nueva URL.
 */
public class Servicio {

    private Long id;
    private String nombre;
    private Long precio;
    private String descripcion;
    private Long tipoServicioId;
    private String urlServicio;
    private Integer estado;

    public Servicio() {}

    // ----------------------------------------------------------------
    // Factory method
    // ----------------------------------------------------------------

    /**
     * Crea un servicio activo en el catálogo.
     * El precio no puede ser negativo — regla de negocio básica.
     */

    public static Servicio crear(String nombre, Long precio, String descripcion, String urlServicio) {
        if (precio != null && precio < 0) {
            throw new IllegalArgumentException("El precio de un servicio no puede ser negativo");
        }
        Servicio servicio = new Servicio();
        servicio.nombre = nombre;
        servicio.precio = precio;
        servicio.descripcion = descripcion;
        servicio.urlServicio = urlServicio;
        servicio.estado = 1;
        return servicio;
    }

    // ----------------------------------------------------------------
    // Comportamiento del dominio
    // ----------------------------------------------------------------

    /** Un servicio está activo si puede ser seleccionado en una reserva. */
    public boolean isActivo() {
        return Integer.valueOf(1).equals(this.estado);
    }

    /** Deshabilita el servicio. Las reservas existentes no se ven afectadas. */
    public void deshabilitar() {
        this.estado = 0;
    }

    /** Actualiza los datos del servicio. Imagen solo si viene nueva. */
    public void actualizar(String nombre, Long precio, String descripcion,
                           Long tipoServicioId, String urlServicio) {
        if (precio != null && precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.nombre         = nombre;
        this.precio         = precio;
        this.descripcion    = descripcion;
        this.tipoServicioId = tipoServicioId;
        if (urlServicio != null) {
            this.urlServicio = urlServicio;
        }
    }

    // ----------------------------------------------------------------
    // Getters y setters
    // ----------------------------------------------------------------

    public Long getId()             { return id; }
    public String getNombre()       { return nombre; }
    public Long getPrecio()         { return precio; }
    public String getDescripcion()  { return descripcion; }
    public Long getTipoServicioId() { return tipoServicioId; }
    public String getUrlServicio()  { return urlServicio; }
    public Integer getEstado()      { return estado; }

    public void setId(Long id)                     { this.id = id; }
    public void setNombre(String nombre)           { this.nombre = nombre; }
    public void setPrecio(Long precio)             { this.precio = precio; }
    public void setDescripcion(String d)           { this.descripcion = d; }
    public void setTipoServicioId(Long id)         { this.tipoServicioId = id; }
    public void setUrlServicio(String url)         { this.urlServicio = url; }
    public void setEstado(Integer estado)          { this.estado = estado; }
}
