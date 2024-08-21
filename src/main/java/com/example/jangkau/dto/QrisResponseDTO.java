package com.example.jangkau.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrisResponseDTO {
    private UUID accountId;
    private String ownerName;
    private String accountNumber;
    private String type;
}
