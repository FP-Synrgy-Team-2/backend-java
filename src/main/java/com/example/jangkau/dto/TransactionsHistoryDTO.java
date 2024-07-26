package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsHistoryDTO {
    
    private UUID transactionId;

    @Nullable
    private AccountResponse from;
    @Nullable
    private AccountResponse to;

    private Date transactionDate;
    private double total;
    private String type;
}
