package com.example.jangkau.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    private UUID userId;
    private String accountNumber;
    private Object accessToken;
    private Object tokenType;
//    @JsonIgnore
    private Object refreshToken;
    private Object expiresIn;
    private Object scope;
    private Object jti;
}
