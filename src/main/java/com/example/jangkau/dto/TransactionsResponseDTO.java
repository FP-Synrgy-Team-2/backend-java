package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;

import com.example.jangkau.models.Account;

import lombok.Data;

@Data
public class TransactionsResponseDTO {
    private UUID transaction_id;
    private Account account;
    private Account beneficiary;
    private double amount;
    private Date transaction_date;
    private String note;
    private boolean is_saved;
    private double admin_fee;
    private double total;
}
