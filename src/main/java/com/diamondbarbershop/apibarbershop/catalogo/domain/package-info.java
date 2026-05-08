/**
 * Bounded Context: CATÁLOGO DE SERVICIOS (Supporting Domain)
 *
 * Responsabilidad: gestionar los servicios que ofrece la barbería y sus precios.
 *
 * Aggregates planeados:
 *   - Servicio (root): nombre, precio vigente, descripción, categoría, estado
 *   - TipoServicio: referenciado por ID desde Servicio
 *
 * Migración prevista: Sprint 2
 *   Mover desde: com.diamondbarbershop.apibarbershop.models.Servicio
 *   Mover desde: com.diamondbarbershop.apibarbershop.services.ServicioService
 */
package com.diamondbarbershop.apibarbershop.catalogo.domain;
