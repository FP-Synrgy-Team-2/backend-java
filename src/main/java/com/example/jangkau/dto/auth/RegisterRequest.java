package com.example.jangkau.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "must not empty")
    private String username;
    @NotEmpty(message = "must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "must not empty")
    private String password;
    @NotBlank(message = "Must not empty")
    private String fullName;
    @NotBlank(message = "Must not empty")
    private String phoneNumber;
}
