package com.example.jangkau.dto.auth;

import com.example.jangkau.services.ValidPassword;
import com.example.jangkau.services.ValidUsername;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "must not empty")
    @ValidUsername
    private String username;
    @NotEmpty(message = "must not empty")
    @ValidPassword
    private String password;
}
