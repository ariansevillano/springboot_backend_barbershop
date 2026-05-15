package com.diamondbarbershop.apibarbershop.security.jwt;

import com.diamondbarbershop.apibarbershop.security.service.CustomUserDetails;
import com.diamondbarbershop.apibarbershop.security.util.ConstantesSeguridad;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Responsabilidad única: GENERAR tokens JWT en el momento del login.
 *
 * La VALIDACIÓN de tokens es responsabilidad de Spring Security (OAuth2 Resource Server).
 * Esta clase ya no necesita validarToken() ni obtenerUsernameDeJwt() — eso lo hace
 * NimbusJwtDecoder configurado en SecurityConfig.
 *
 * API jjwt 0.12.x: sin métodos deprecated, sin SignatureAlgorithm estático,
 * la clave es un SecretKey tipado (no un String suelto).
 */
@Component
public class JwtGenerador {

    // La clave se crea UNA VEZ al arrancar. Keys.hmacShaKeyFor valida
    // que el array de bytes tenga longitud mínima para el algoritmo.
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            ConstantesSeguridad.JWT_FIRMA.getBytes(StandardCharsets.UTF_8)
    );


    public String generarToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return construirToken(
                    userDetails.getUsername(),
                    userDetails.getNombre(),
                    userDetails.getApellido(),
                    obtenerRol(authentication),
                    userDetails.getUrlUsuario()
            );
        } else if (authentication.getPrincipal() instanceof String username) {
            return construirToken(username, null, null, obtenerRol(authentication), null);
        }
        throw new IllegalArgumentException(
                "Principal no reconocido: " + authentication.getPrincipal().getClass()
        );
    }


    private String construirToken(String username, String nombre, String apellido,
                                  String rol, String urlUsuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                // API 0.12.x: sin prefijo "set"
                .subject(username)
                .claim("nombre", nombre)
                .claim("apellido", apellido)
                .claim("rol", rol)
                .claim("urlUsuario", urlUsuario)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusMillis(ConstantesSeguridad.JWT_EXPIRATION_TOKEN)))
                // signWith solo necesita la clave — el algoritmo se infiere del tipo de clave
                .signWith(secretKey)
                .compact();
    }

    private String obtenerRol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");
    }
}
