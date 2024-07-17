package com.example.jangkau.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinValidationDTO {
    private String accountNumber;
    private String pin;
}