package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class QrisTransactionRequest {
    
    private UUID accountId;
    private UUID beneficiaryAccount;
    private double amount;
}
