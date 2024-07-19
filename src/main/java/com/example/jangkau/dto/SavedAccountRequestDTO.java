package com.example.jangkau.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavedAccountRequestDTO {
    private UUID userId;
    private UUID accountId;
}
