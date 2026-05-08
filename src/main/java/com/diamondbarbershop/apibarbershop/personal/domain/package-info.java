/**
 * Bounded Context: PERSONAL (Supporting Domain)
 *
 * Responsabilidad: gestionar a los barberos y su estado laboral.
 *
 * Aggregates planeados:
 *   - Barbero (root): nombre, estado (activo/inactivo), urlFoto
 *
 * Relación con Agenda:
 *   Barbero existe en Personal. Agenda solo usa BarberoId como referencia.
 *
 * Migración prevista: Sprint 2
 *   Mover desde: com.diamondbarbershop.apibarbershop.models.Barbero
 *   Mover desde: com.diamondbarbershop.apibarbershop.services.BarberoService
 */
package com.diamondbarbershop.apibarbershop.personal.domain;
