package com.example.jangkau.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private UUID accountId;
    private String ownerName;
    private String accountNumber;
}
