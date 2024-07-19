package com.example.jangkau.dto;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountResponseDTO {
    private UUID userId;
    private String ownerName;
    private String accountNumber;
    private double balance;
}
