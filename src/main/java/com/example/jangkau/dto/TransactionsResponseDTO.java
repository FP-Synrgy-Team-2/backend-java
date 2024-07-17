package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsResponseDTO {
    private UUID transaction_id;
    private UUID account_id;
    private UUID beneficiary_account;
    private double amount;
    private Date transaction_date;
    private String note;
    private double admin_fee;
    private double total;
}
