package com.example.jangkau.controllers;

import java.util.HashMap;
import java.util.Map;

import com.example.jangkau.models.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Account;
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
            Account account_id = accountService.getAccountById(newTransaction.getAccount_id().toString());
            Account beneficiary = accountService.getAccountById(newTransaction.getBeneficiary_account().toString());
            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            trans.setAccount_id(account_id);
            trans.setBeneficiary_account(beneficiary);
            accountService.updateBalance(trans);
        }
        response.put("status", "suscces");
        response.put("data", newTransaction);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/status/{transaction_id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> getTransactionStatus(@PathVariable("transaction_id") String transactionId){
        Map<String, Object> response = new HashMap<>();

        Transactions transaction;
        try {
            transaction = transactionService.getTransaction(transactionId);
            response.put("status", "transaction success");
            response.put("data", modelMapper.map(transaction, TransactionsResponseDTO.class));
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.put("status", "error, transaction not found");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
