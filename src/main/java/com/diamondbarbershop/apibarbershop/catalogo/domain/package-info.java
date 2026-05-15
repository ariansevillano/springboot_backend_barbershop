/**
 * Bounded Context: CATÁLOGO DE SERVICIOS (Supporting Domain)
 *
 * Responsabilidad: gestionar los servicios que ofrece la barbería y sus precios.
 *
 * Aggregates planeados:
 *   - ServicioEntity (root): nombre, precio vigente, descripción, categoría, estado
 *   - TipoServicio: referenciado por ID desde ServicioEntity
 *
 * Migración prevista: Sprint 2
 *   Mover desde: com.diamondbarbershop.apibarbershop.models.ServicioEntity
 *   Mover desde: com.diamondbarbershop.apibarbershop.services.ServicioService
 */
package com.diamondbarbershop.apibarbershop.catalogo.domain;
