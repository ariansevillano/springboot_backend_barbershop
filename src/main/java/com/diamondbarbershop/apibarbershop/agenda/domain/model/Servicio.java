package com.diamondbarbershop.apibarbershop.agenda.domain.model;

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

}
