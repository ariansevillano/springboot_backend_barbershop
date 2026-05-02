package com.diamondbarbershop.apibarbershop.security.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private final String nombre;
    private final String apellido;
    private final String urlUsuario;

    public CustomUserDetails(String username, String password, String nombre, String apellido, @Email(message = "El correo electrónico no es válido") @NotBlank(message = "El campo email no puede estar vacío") String email, Collection<? extends GrantedAuthority> authorities, String urlUsuario) {
        super(username, password, authorities);
        this.nombre = nombre;
        this.apellido = apellido;
        this.urlUsuario = urlUsuario;
    }

}

