/**
 * Bounded Context: VALORACIONES (Supporting Domain)
 *
 * Responsabilidad: gestionar reseñas y calificaciones de clientes.
 *
 * Aggregates planeados:
 *   - Valoracion (root): puntuación numérica, mensaje, flag "útil", estado
 *     Referencias externas: clienteId (de Identidad)
 *
 * Migración prevista: Sprint 2
 *   Mover desde: com.diamondbarbershop.apibarbershop.models.ValoracionEntity
 *   Mover desde: com.diamondbarbershop.apibarbershop.services.ValoracionService
 */
package com.diamondbarbershop.apibarbershop.valoraciones.domain;
