package com.example.jangkau.dto;

import com.example.jangkau.services.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Must not empty")
    private String username;
    @NotBlank(message = "Must not empty")
    private String emailAddress;
    @NotBlank(message = "Must not empty")
    @ValidPassword
    private String password;
    @NotBlank(message = "Must not empty")
    private String fullName;
    @NotBlank(message = "Must not empty")
    private String phoneNumber;
}
