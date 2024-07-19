package com.example.jangkau.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavedAccountResponseDTO {
    private UUID savedAccountId;
    private String ownerName;
    private String accountNumber;
}
