package com.example.jangkau.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "must not empty")
    private String username;
    @NotEmpty(message = "must not empty")
    private String password;
}
