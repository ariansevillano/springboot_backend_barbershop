package com.diamondbarbershop.apibarbershop.valoraciones.domain.model;

/**
 * Modelo de dominio del Bounded Context "Valoraciones".
 *
 * Gestiona las reseñas y calificaciones que los clientes dejan
 * sobre el servicio recibido.
 *
 * Reglas de negocio:
 *   1. Una valoración nueva siempre inicia activa (visible).
 *   2. El admin puede desactivarla (moderación), no eliminarla.
 *   3. La calificación debe estar entre 1 y 5.
 */
public class Valoracion {

    private Long id;
    private Integer puntuacion;  // 1 a 5
    private Boolean util;
    private String mensaje;
    private Long clienteId;      // referencia a BC Identidad — solo el ID
    private Integer estado;

    public Valoracion() {}

    // ----------------------------------------------------------------
    // Factory method
    // ----------------------------------------------------------------

    /**
     * Crea una valoración activa.
     * La puntuación debe estar entre 1 y 5 — regla del dominio.
     */
    public static Valoracion crear(Integer puntuacion, Boolean util,
                                   String mensaje, Long clienteId) {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException(
                    "La puntuación debe estar entre 1 y 5. Valor recibido: " + puntuacion);
        }
        Valoracion v = new Valoracion();
        v.puntuacion = puntuacion;
        v.util       = util;
        v.mensaje    = mensaje;
        v.clienteId  = clienteId;
        v.estado     = 1; // activa por defecto
        return v;
    }

    // ----------------------------------------------------------------
    // Comportamiento del dominio
    // ----------------------------------------------------------------

    /** Una valoración activa es visible públicamente en el sistema. */
    public boolean isActiva() {
        return Integer.valueOf(1).equals(this.estado);
    }

    /**
     * El administrador desactiva la valoración (moderación por contenido
     * inapropiado u otras razones). No se elimina para mantener el registro.
     */
    public void desactivar() {
        this.estado = 0;
    }

    // ----------------------------------------------------------------
    // Getters y setters
    // ----------------------------------------------------------------

    public Long getId()            { return id; }
    public Integer getPuntuacion() { return puntuacion; }
    public Boolean getUtil()       { return util; }
    public String getMensaje()     { return mensaje; }
    public Long getClienteId()     { return clienteId; }
    public Integer getEstado()     { return estado; }

    public void setId(Long id)              { this.id = id; }
    public void setPuntuacion(Integer p)    { this.puntuacion = p; }
    public void setUtil(Boolean util)       { this.util = util; }
    public void setMensaje(String m)        { this.mensaje = m; }
    public void setClienteId(Long id)       { this.clienteId = id; }
    public void setEstado(Integer estado)   { this.estado = estado; }
}
