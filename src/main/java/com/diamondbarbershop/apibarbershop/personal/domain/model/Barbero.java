package com.diamondbarbershop.apibarbershop.personal.domain.model;

/**
 * Modelo de dominio del Bounded Context "Personal".
 *
 * IMPORTANTE: Esta clase NO tiene @Entity ni ninguna anotación de JPA.
 * Es dominio puro — no sabe que existe una base de datos.
 * La persistencia la maneja la capa de infraestructura (models/Barbero.java
 * actúa como entidad JPA mientras completamos la migración).
 *
 * Reglas de negocio que protege esta clase:
 *   1. Un barbero nuevo siempre inicia en estado activo.
 *   2. Solo se puede deshabilitar, no eliminar (baja lógica).
 *   3. La actualización de imagen solo ocurre si se provee una nueva URL.
 */

public class Barbero {

    private Long id;
    private String nombre;
    private Integer estado;
    private String urlBarbero;

    // Constructor vacío requerido para mapeos
    public Barbero() {}

    // ----------------------------------------------------------------
    // Factory method — la única forma correcta de crear un barbero nuevo
    // ----------------------------------------------------------------

    /**
     * Crea un barbero nuevo en estado activo.
     * El estado inicial siempre es 1 (activo) — no se puede crear un barbero
     * deshabilitado porque no tendría sentido de negocio.
     */

    public static Barbero crear(String nombre, String urlBarbero) {
        Barbero barbero = new Barbero();
        barbero.nombre = nombre;
        barbero.urlBarbero = urlBarbero;
        barbero.estado = 1; //activo por definición
        return barbero;
    }

    // ----------------------------------------------------------------
    // Comportamiento del dominio
    // ----------------------------------------------------------------

    /**
     * Regla de negocio: un barbero "está activo" cuando estado == 1.
     * Usamos un método con nombre intencional en lugar de exponer
     * el campo entero directamente.
     */

    public boolean isActivo() {
        return Integer.valueOf(1).equals(this.estado);
    }

    /**
     * Deshabilita el barbero del sistema (baja lógica).
     * No se elimina de la BD para preservar el historial de reservas.
     */
    public void deshabilitar() {
        this.estado = 0;
    }

    /**
     * Actualiza los datos del barbero.
     * Si no se provee nueva imagen (null), conserva la anterior.
     */
    public void actualizar(String nombre, String urlBarbero) {
        this.nombre = nombre;
        if (urlBarbero != null) {
            this.urlBarbero = urlBarbero;
        }
    }

    // ----------------------------------------------------------------
    // Getters y setters (necesarios para el mapeo entre capas)
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getEstado() { return estado; }
    public String getUrlBarbero() { return urlBarbero; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEstado(Integer estado) { this.estado = estado; }
    public void setUrlBarbero(String urlBarbero) { this.urlBarbero = urlBarbero; }
}
