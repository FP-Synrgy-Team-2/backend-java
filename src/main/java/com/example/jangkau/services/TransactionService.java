package com.example.jangkau.services;

import java.util.List;

import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Transactions;

public interface TransactionService {

    List<Transactions> getAllTransactions(String accountNumber);

    TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO);
    
    Transactions getTransaction(String transactionId);
}
