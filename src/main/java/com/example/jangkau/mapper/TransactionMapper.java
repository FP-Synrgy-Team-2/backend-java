package com.example.jangkau.mapper;

import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Transactions;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionsResponseDTO toTransactionResponse(Transactions transactions) {
        return TransactionsResponseDTO.builder()
                .transaction_id(transactions.getTransaction_id())
                .account_id(transactions.getAccount_id().getId())
                .beneficiary_account(transactions.getBeneficiary_account().getId())
                .amount(transactions.getAmount())
                .admin_fee(transactions.getAdmin_fee())
                .transaction_date(transactions.getTransaction_date())
                .note(transactions.getNote())
                .total(transactions.getAmount() + transactions.getAdmin_fee())
                .build();
    }

   
}
