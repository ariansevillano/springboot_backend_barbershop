package com.diamondbarbershop.apibarbershop.security.util;

public class ConstantesSeguridad {
    public static final long JWT_EXPIRATION_TOKEN = 300000; //equivale a 5 min, donde 60000 = a 1 min
    public static final String JWT_FIRMA = "firma";

    public static final long JWT_REFRESH_EXPIRATION = 604800000; // 7 días

}
