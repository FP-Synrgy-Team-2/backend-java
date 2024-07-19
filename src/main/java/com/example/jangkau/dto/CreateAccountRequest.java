package com.example.jangkau.dto;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateAccountRequest {

    private String username;

    private String password;

    private String ownerName;

    private Integer pin;

    private Double balance;
}
