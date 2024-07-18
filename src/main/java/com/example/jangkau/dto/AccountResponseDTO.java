package com.example.jangkau.dto;


import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {
    private UUID id;
    private String ownerName;
    private String accountNumber;
    private double balance;
}
