package com.diamondbarbershop.apibarbershop.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuario_id;
    @NotBlank(message = "El campo username no puede estar vacío")
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    @NotBlank(message = "El campo password no puede estar vacío")
    private String password;
    @Column(nullable = false)
    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String nombre;
    @Column(nullable = false)
    @NotBlank(message = "El campo apellido no puede estar vacío")
    private String apellido;
    @Column(nullable = false)
    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El campo email no puede estar vacío")
    private String email;
    @NotBlank(message = "El campo celular no puede estar vacío")
    private String celular;
    @Column(nullable = true)
    private String urlUsuario;
    private String tokenPassword;
    @Column(name = "last_token_request")
    private LocalDateTime lastTokenRequest;
    private String refreshToken; //Refresh Token
    private LocalDateTime refreshTokenExpiryDate; //Fecha de expiración del refresh token
    //Usamos fetchType en EAGER para que cada vez que se acceda o se extraiga un usuario de la BD, este se traiga todos sus roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    /*Con JoinTable estaremos creando una tabla que unirá la tabla de usuario y rol, con lo cual tendremos un total de 3 tablas
    relacionadas en la tabla "usuarios_roles", a través de sus columnas usuario_id que apuntara al ID de la tabla usuario
    y rol_id que apuntara al Id de la tabla role */
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    ,inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "rol_id"))
    private List<Rol> roles = new ArrayList<>();
}
