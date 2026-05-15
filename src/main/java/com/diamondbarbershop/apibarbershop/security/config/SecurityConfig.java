package com.diamondbarbershop.apibarbershop.security.config;

import com.diamondbarbershop.apibarbershop.security.jwt.JwtAuthenticationEntryPoint;
import com.diamondbarbershop.apibarbershop.security.util.ConstantesSeguridad;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

//Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicación
@Configuration
//Indicamos que se activa la seguridad web en nuestra aplicación y además esta será una clase la cual contendrá toda la configuración referente a la seguridad
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Este bean va a encargarse de verificar la información de los usuarios que se loguearán en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Decodificador de JWT — Spring Security lo usa automáticamente para
     * validar cada token entrante. Reemplaza al JwtAuthenticationFilter manual.
     *
     * MacAlgorithm.HS512 debe coincidir con el algoritmo usado en JwtGenerador.
     */
    @Bean
    NimbusJwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(
                ConstantesSeguridad.JWT_FIRMA.getBytes(StandardCharsets.UTF_8)
        );
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS512)
                .build();
    }

    /**
     * Convierte los claims del JWT en GrantedAuthorities de Spring Security.
     *
     * El JWT tiene un claim "rol" con valor "ADMIN" o "USER".
     * Sin este converter, Spring Security buscaría "scope" o "scp" y añadiría
     * el prefijo "SCOPE_" — lo que rompería nuestros hasAuthority("ADMIN").
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String rol = jwt.getClaimAsString("rol");
            if (rol == null || rol.isBlank()) return List.of();
            return List.of(new SimpleGrantedAuthority(rol));
        });
        return converter;
    }

    //Vamos a crear un bean el cual va a establecer una cadena de filtros de seguridad en nuestra aplicación.
    // Y es aquí donde determinaremos los permisos segun los roles de usuarios para acceder a nuestra aplicación
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "emailPassword/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/servicio/crear").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/servicio/listar").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/servicio/listarId/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicio/eliminar/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/servicio/actualizar").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/v1/registerAdm").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/refreshToken").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/barbero/crear").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/barbero/listar").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/barbero/listarId/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbero/eliminar/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/barbero/actualizar").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/valoracion/crear").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/valoracion/listar").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "api/valoracion/responder").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/horarioBarberoBase/actualizarTurnosDia").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/horarioBarberoBase/confirmarHorario").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/horarioInstancia/actual").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/rango/listar").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/rango/listarId/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/reportes/horario").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/listar").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/listarId/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/actualizar/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/reserva/crear").hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST, "/api/reserva/subir-comprobante/**").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/reserva/admin/listar").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/reserva/admin/cambiar-estado/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/reserva/barberos-disponibles").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/reserva/mis-reservas").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "api/reserva/consultarRecompensa").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "api/reserva/crearReservaRecompensa").hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                // OAuth2 Resource Server reemplaza al JwtAuthenticationFilter manual.
                // Spring Security valida la firma, expiración y claims del JWT automáticamente.
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );
        // Ya NO hay http.addFilterBefore(jwtAuthenticationFilter(), ...)
        return http.build();
    }
}
