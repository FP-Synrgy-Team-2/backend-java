package com.example.jangkau.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountResponse {
    private String accountNumber;

    private String username;

    private String ownerName;

    private Double balance;
}
