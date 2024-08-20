package com.example.jangkau.services;


import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.jangkau.dto.DateFilterRequestDTO;
import com.example.jangkau.dto.TransactionsHistoryDTO;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Transactions;

public interface TransactionService {

    TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO, Principal principal);
    
    Transactions getTransaction(String transactionId);

    List<TransactionsHistoryDTO> getTransactionByDate(String userId, DateFilterRequestDTO request);
}
