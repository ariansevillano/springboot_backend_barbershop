package com.diamondbarbershop.apibarbershop.security.config;

import com.diamondbarbershop.apibarbershop.security.jwt.JwtAuthenticationEntryPoint;
import com.diamondbarbershop.apibarbershop.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicación
@EnableWebSecurity
//Indicamos que se activa la seguridad web en nuestra aplicación y además esta será una clase la cual contendrá toda la configuración referente a la seguridad
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Este bean va a encargarse de verificar la información de los usuarios que se loguearán en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    //Vamos a crear un bean el cual va a establecer una cadena de filtros de seguridad en nuestra aplicación.
    // Y es aquí donde determinaremos los permisos segun los roles de usuarios para acceder a nuestra aplicación
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http

                .csrf().disable()
                .exceptionHandling() //Permitimos el manejo de excepciones
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //Nos establece un punto de entrada personalizado de autenticación para el manejo de autenticaciones no autorizadas
                .and()
                .sessionManagement() //Permite la gestión de sessiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() //Toda petición http debe ser autorizada
                .requestMatchers("/api/auth/**",
                                "emailPassword/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/servicio/crear").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/servicio/listar").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.GET,"/api/servicio/listarId/**").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.DELETE,"/api/servicio/eliminar/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/servicio/actualizar").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/auth/v1/registerAdm").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/auth/register").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/auth/refreshToken").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/barbero/crear").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/barbero/listar").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.GET,"/api/barbero/listarId/**").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.DELETE,"/api/barbero/eliminar/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/barbero/actualizar").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/valoracion/crear").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET,"/api/valoracion/listar").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"api/valoracion/responder").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/horarioBarberoBase/actualizarTurnosDia").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/horarioBarberoBase/confirmarHorario").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/horarioInstancia/actual").hasAnyAuthority("ADMIN","USER")
                .requestMatchers(HttpMethod.GET,"/api/rango/listar").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.GET,"/api/rango/listarId/**").hasAnyAuthority("ADMIN" , "USER")
                .requestMatchers(HttpMethod.GET,"/api/reportes/horario").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/usuario/listar").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/usuario/listarId/**").hasAnyAuthority("ADMIN","USER")
                .requestMatchers(HttpMethod.PUT,"/api/usuario/actualizar/**").hasAnyAuthority("ADMIN", "USER")
                // Reservas: solo USER puede crear y subir comprobante
                .requestMatchers(HttpMethod.POST, "/api/reserva/crear").hasAuthority("USER")
                .requestMatchers(HttpMethod.POST, "/api/reserva/subir-comprobante/**").hasAuthority("USER")
                // Reservas: solo ADMIN puede listar y cambiar estado
                .requestMatchers(HttpMethod.GET, "/api/reserva/admin/listar").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/reserva/admin/cambiar-estado/**").hasAuthority("ADMIN")
                // Barberos disponibles: tanto USER como ADMIN pueden consultar
                .requestMatchers(HttpMethod.GET, "/api/reserva/barberos-disponibles").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reserva/mis-reservas").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET,"api/reserva/consultarRecompensa").hasAuthority("USER")
                .requestMatchers(HttpMethod.GET,"api/reserva/crearReservaRecompensa").hasAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
