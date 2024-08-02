package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;




import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsResponseDTO {
    private UUID transactionId;
    private UUID accountId;
    private AccountResponse beneficiaryAccount;
    private double amount;
    private Date transactionDate;
    private String note;
    private double adminFee;
    private double total;
}
