package com.diamondbarbershop.apibarbershop.exceptions;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException (String message) {
        super(message);
    }
}
