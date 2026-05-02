package com.diamondbarbershop.apibarbershop.emailPassword.dto;


import lombok.Data;

@Data
public class EmailDto {

    private String mailFrom;
    private String mailTo;
    private String subject;
    private String username;
    private String nombre;
    private String tokenPassword;

}
