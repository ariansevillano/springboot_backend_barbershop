package com.diamondbarbershop.apibarbershop.dtos.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoLoginResponse {
    private String token;
    private String refreshToken;
}