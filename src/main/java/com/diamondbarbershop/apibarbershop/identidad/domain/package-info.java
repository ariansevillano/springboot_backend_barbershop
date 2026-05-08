/**
 * Bounded Context: IDENTIDAD Y ACCESO (Supporting Domain)
 *
 * Responsabilidad: autenticación, autorización y gestión de cuentas de usuario.
 *
 * Aggregates planeados:
 *   - Usuario (root): credenciales, perfil, roles, tokens de seguridad
 *   - Rol: ADMIN, BARBERO, CLIENTE
 *
 * Este contexto es "Supplier" para todos los demás:
 *   - Provee ClienteId (para Reservas y Valoraciones)
 *   - El JWT token autentica el acceso a todos los endpoints
 *
 * Migración prevista: Sprint 2
 *   Mover desde: com.diamondbarbershop.apibarbershop.models.Usuario
 *   Mover desde: com.diamondbarbershop.apibarbershop.services.AuthService
 *   Mover desde: com.diamondbarbershop.apibarbershop.security.*
 */
package com.diamondbarbershop.apibarbershop.identidad.domain;
