package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsRequestDTO {
    private UUID account_id;
    private UUID beneficiary_account;
    private double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date transaction_date;
    private String note;
    private boolean is_saved;
}
 