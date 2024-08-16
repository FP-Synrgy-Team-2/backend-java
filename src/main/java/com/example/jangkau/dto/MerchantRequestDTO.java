package com.example.jangkau.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class MerchantRequestDTO {
    private String name;
    private UUID accountId;

}
