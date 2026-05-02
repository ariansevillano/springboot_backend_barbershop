package com.diamondbarbershop.apibarbershop.dtos.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DtoRefreshToken {
    @NotBlank(message = "El refresh token no puede estar vacío")
    private String refreshToken;
}
