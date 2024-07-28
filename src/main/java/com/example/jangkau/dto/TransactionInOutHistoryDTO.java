package com.example.jangkau.dto;

import lombok.Data;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class TransactionInOutHistoryDTO {
    private UUID transactionId;
    private String amount;
    private UUID sourceId;
    private UUID beneficiaryId;
    private Date transactionDate;
    private String note;
}
