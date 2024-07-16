package com.example.jangkau.controllers;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.TransactionService;



@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired TransactionService transactionService;
    @Autowired AccountService accountService;
    @Autowired ModelMapper modelMapper;


    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> createNewTransaction(@RequestBody TransactionsRequestDTO transactionsRequestDTO){
        Map<String, Object> response = new HashMap<>();

        TransactionsResponseDTO newTransaction = transactionService.createTransaction(transactionsRequestDTO);
        if (newTransaction != null) {
            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            accountService.updateBalance(trans);
        }
        response.put("status", "suscces");
        response.put("data", newTransaction);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
