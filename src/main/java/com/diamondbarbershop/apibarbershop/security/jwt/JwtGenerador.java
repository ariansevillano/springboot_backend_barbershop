package com.diamondbarbershop.apibarbershop.security.jwt;

import com.diamondbarbershop.apibarbershop.security.service.CustomUserDetails;
import com.diamondbarbershop.apibarbershop.security.util.ConstantesSeguridad;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerador {


    //Método para crear un token por medio de la authentication
    /*public String generarToken(Authentication authentication) {
        CustomUsersDetailsService.CustomUserDetails userDetails = (CustomUsersDetailsService.CustomUserDetails) authentication.getPrincipal();

        String username = authentication.getName();
        String nombre = userDetails.getNombre();
        String apellido = userDetails.getApellido();
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("UNKNOWN");
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        //Linea para generar el token
        String token = Jwts.builder() //Construimos un token JWT llamado token
                .setSubject(username)//Aca establecemos el nombre de usuario que está iniciando sesión
                .claim("nombre",nombre)
                .claim("apellido",apellido)
                .claim("rol", rol) // Agregar el rol como claim
                .setIssuedAt(new Date()) //Establecemos la fecha de emisión del token en el momento actual
                .setExpiration(expiracionToken) //Establecemos la fecha de caducidad del token
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA) //Utilizamos este método para firmar
                //nuestro token y de esta manera evitar la manipulación o modificación de este
                .compact(); //Este método finaliza la construcción del token y lo convierte en una cadena compacta
        return token;
    }*/


    private String generarTokenDesdeCustomUserDetails(CustomUserDetails userDetails, Authentication authentication){
        String username = userDetails.getUsername();
        String nombre = userDetails.getNombre();
        String apellido = userDetails.getApellido();
        String rol = obtenerRol(authentication);
        String urlUsuario = userDetails.getUrlUsuario();
        return construirToken(username,nombre,apellido,rol,urlUsuario);

    }

    private String generarTokenDesdeUsername(String username,Authentication authentication){
        String rol = obtenerRol(authentication);
        return construirToken(username,null,null,rol,null);
    }

    private String obtenerRol(Authentication authentication){
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("UNKNOWN");
    }

    private String construirToken(String username, String nombre, String apellido, String rol, String urlUsuario){
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        return Jwts.builder()
                .setSubject(username)
                .claim("nombre",nombre)
                .claim("apellido",apellido)
                .claim("rol",rol)
                .claim("urlUsuario", urlUsuario)
                .setIssuedAt(tiempoActual)
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
    }

    public String generarToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return generarTokenDesdeCustomUserDetails(userDetails, authentication);
        } else if (authentication.getPrincipal() instanceof String username) {
            return generarTokenDesdeUsername(username, authentication);
        } else {
            throw new IllegalArgumentException("El principal no es de un tipo esperado.");
        }
    }

    /*public String generarToken(Authentication authentication) {
        String username;
        String nombre = null;
        String apellido = null;
        String rol;

        // Validar el tipo de principal
        if (authentication.getPrincipal() instanceof CustomUsersDetailsService.CustomUserDetails userDetails) {
            username = userDetails.getUsername();
            nombre = userDetails.getNombre();
            apellido = userDetails.getApellido();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        } else {
            throw new IllegalArgumentException("El principal no es de un tipo esperado.");
        }

        // Obtener el rol
        rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("UNKNOWN");

        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        // Generar el token JWT
        String token = Jwts.builder()
                .setSubject(username)
                .claim("nombre", nombre)
                .claim("apellido", apellido)
                .claim("rol", rol)
                .setIssuedAt(tiempoActual)
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();

        return token;
    }*/

    //Método para extraer un Username apartir de un token
    public String obtenerUsernameDeJwt(String token) {
        Claims claims = Jwts.parser() // El método parser se utiliza con el fin de analizar el token
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)// Establece la clave de firma, que se utiliza para verificar la firma del token
                .parseClaimsJws(token) //Se utiliza para verificar la firma del token, apartir del String "token"
                .getBody(); /*Obtenemos el claims(cuerpo) ya verificado del token el cual contendrá la información de
                nombre de usuario, fecha de expiración y firma del token*/
        return claims.getSubject(); //Devolvemos el nombre de usuario
    }

    //Método para validar el token
    public Boolean validarToken(String token) {
        try {
            //Validación del token por medio de la firma que contiene el String token(token)
            //Si son idénticas validara el token o caso contrario saltara la excepción de abajo
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt expiró o esta incorrecto");
        }
    }
}
