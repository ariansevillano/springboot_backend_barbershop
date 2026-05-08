package com.diamondbarbershop.apibarbershop.reservas.domain.model;

import java.util.Objects;

/**
 * Value Object: representa el precio de un servicio en el momento de la reserva.
 *
 * ¿Por qué es un Value Object y no un simple Long?
 *   - Encapsula la REGLA DE NEGOCIO: el precio no puede ser negativo ni nulo.
 *   - Es INMUTABLE: si el precio "cambia", se crea una nueva instancia, no se modifica.
 *   - La igualdad se basa en el VALOR: Precio(50) == Precio(50), sin importar qué objeto es.
 *
 * Diferencia clave con Entity:
 *   Entity: id=1, monto=50  ≠  id=2, monto=50  (son distintas aunque el valor sea igual)
 *   Value Object: Precio(50) == Precio(50)       (son la misma cosa si el valor es igual)
 *
 * NOTA: El precio se "congela" en la reserva al momento de crearla.
 * Si el precio del servicio en el catálogo cambia después, la reserva mantiene
 * el precio original — esto es intencional y correcto en el negocio.
 */
public final class Precio {

    private final Long monto;

    public Precio(Long monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El precio no puede ser nulo");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo. Valor recibido: " + monto);
        }
        this.monto = monto;
    }

    public Long getMonto() {
        return monto;
    }

    // Igualdad por VALOR (no por referencia como haría Object.equals por defecto)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Precio precio)) return false;
        return Objects.equals(monto, precio.monto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto);
    }

    @Override
    public String toString() {
        return "S/. " + monto;
    }
}
