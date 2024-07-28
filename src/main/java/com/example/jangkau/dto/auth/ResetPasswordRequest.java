package com.example.jangkau.dto.auth;

import com.example.jangkau.services.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "Must not empty")
    @Email
    private String emailAddress;
    @NotEmpty(message = "Must not empty")
    private String otp;
    @NotEmpty(message = "Must not empty")
    @ValidPassword
    private String newPassword;
}
