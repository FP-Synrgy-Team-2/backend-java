package com.example.jangkau.dto;

import java.util.Date;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TransactionsRequestDTO {
    private UUID accountId;
    private UUID beneficiaryAccount;
    private double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date transactionDate;
    private String note;
    private boolean isSaved;
}
 