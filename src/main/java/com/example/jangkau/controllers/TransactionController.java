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

import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.SavedAccountService;
import com.example.jangkau.services.TransactionService;
import com.example.jangkau.services.UserService;



@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired TransactionService transactionService;
    @Autowired AccountService accountService;
    @Autowired ModelMapper modelMapper;
    @Autowired SavedAccountService savedAccountService;
    @Autowired UserService userService;


    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> createNewTransaction(@RequestBody TransactionsRequestDTO transactionsRequestDTO){
        Map<String, Object> response = new HashMap<>();
        TransactionsResponseDTO newTransaction = transactionService.createTransaction(transactionsRequestDTO);
        if (newTransaction != null) {
            Account accountId = accountService.getAccountById(newTransaction.getAccountId().toString());
            Account beneficiary = accountService.getAccountById(newTransaction.getBeneficiaryAccount().toString());
            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            trans.setAccountId(accountId);
            trans.setBeneficiaryAccount(beneficiary);
            accountService.updateBalance(trans);
            if (transactionsRequestDTO.isSaved()) {
                Account account = accountService.getAccountById(transactionsRequestDTO.getBeneficiaryAccount().toString());
                Account userAccount = accountService.getAccountById(transactionsRequestDTO.getAccountId().toString());
                UserResponse user = userService.findById(userAccount.getUser().getId());
                SavedAccountRequestDTO request = SavedAccountRequestDTO.builder()
                    .accountId(account.getId())
                    .userId(user.getId())
                    .build();
                savedAccountService.createSavedAccount(request);
            }
        }
        response.put("status", "success");
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
