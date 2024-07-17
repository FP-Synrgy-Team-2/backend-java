package com.example.jangkau.dto;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@Builder
public class CreateAccountRequest {
    private UserRequest userRequest;

    private String ownerName;

    private Integer pin;

    @Nullable
    private Double balance;
}
