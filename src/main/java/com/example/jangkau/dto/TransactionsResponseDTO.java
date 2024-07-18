package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class TransactionsResponseDTO {
    private UUID transactionId;
    private UUID accountId;
    private UUID beneficiaryAccount;
    private double amount;
    private Date transactionDate;
    private String note;
    private double adminFee;
    private double total;
}
