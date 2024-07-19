package com.example.jangkau.mapper;

import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Transactions;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionsResponseDTO toTransactionResponse(Transactions transactions) {
        return TransactionsResponseDTO.builder()
                .transactionId(transactions.getTransactionId())
                .accountId(transactions.getAccountId().getId())
                .beneficiaryAccount(transactions.getBeneficiaryAccount().getId())
                .amount(transactions.getAmount())
                .adminFee(transactions.getAdminFee())
                .transactionDate(transactions.getTransactionDate())
                .note(transactions.getNote())
                .total(transactions.getAmount() + transactions.getAdminFee())
                .build();
    }

   
}
