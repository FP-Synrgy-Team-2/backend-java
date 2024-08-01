package com.example.jangkau.mapper;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.TransactionsHistoryDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;
import com.example.jangkau.models.Transactions;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionsResponseDTO toTransactionResponse(Transactions transactions) {
        return TransactionsResponseDTO.builder()
                .transactionId(transactions.getTransactionId())
                .accountId(transactions.getAccountId().getId())
                .beneficiaryAccount(toAccountResponse(transactions.getBeneficiaryAccount()))
                .amount(transactions.getAmount())
                .adminFee(transactions.getAdminFee())
                .transactionDate(transactions.getTransactionDate())
                .note(transactions.getNote())
                .total(transactions.getAmount() + transactions.getAdminFee())
                .build();
    }

    public TransactionsHistoryDTO toTransactionsHistory(Transactions transactions, UUID accountId){
        TransactionsHistoryDTO response =  TransactionsHistoryDTO.builder()
            .transactionId(transactions.getTransactionId())
            .total(transactions.getAmount() + transactions.getAdminFee())
            .transactionDate(transactions.getTransactionDate())
            .from(toAccountResponse(transactions.getAccountId()))
            .to(toAccountResponse(transactions.getBeneficiaryAccount()))
            .build();
        
        if (accountId == transactions.getAccountId().getId()) {
            response.setType("Pengeluaran");
        }else if(accountId == transactions.getBeneficiaryAccount().getId()){
            response.setType("Pemasukan");
        }
        return response;
    }

    public AccountResponse toAccountResponse(Account account){
        return AccountResponse.builder()
            .accountId(account.getId())
            .ownerName(account.getOwnerName())
            .accountNumber(account.getAccountNumber())
            .build();
    } 


   
}
