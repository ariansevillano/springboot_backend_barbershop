package com.diamondbarbershop.apibarbershop.security.util;

public class ConstantesSeguridad {
    public static final long JWT_EXPIRATION_TOKEN = 300000; //equivale a 5 min, donde 60000 = a 1 min
    //harcodeado, solo para pruebas
    public static final String JWT_FIRMA = "DiamondBarberHub-Secret-Key-For-JWT-Signing-HS512-Algorithm-Needs-64-Bytes-Min!!";
    public static final long JWT_REFRESH_EXPIRATION = 604800000; // 7 días
}
