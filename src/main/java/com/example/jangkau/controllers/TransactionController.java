package com.example.jangkau.controllers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.jangkau.dto.DateFilterRequestDTO;
import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.TransactionsHistoryDTO;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
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
    public ResponseEntity<Map<String, Object>> createNewTransaction(@RequestBody TransactionsRequestDTO transactionsRequestDTO){
        Map<String, Object> response = new HashMap<>();
        TransactionsResponseDTO newTransaction = transactionService.createTransaction(transactionsRequestDTO);
        if (newTransaction != null) {
            Account accountId = accountService.getAccountByAccountId(newTransaction.getAccountId());
            Account beneficiary = accountService.getAccountByAccountId(newTransaction.getBeneficiaryAccount().getAccountId());

            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            trans.setAccountId(accountId);
            trans.setBeneficiaryAccount(beneficiary);
            accountService.updateBalance(trans);
            if (transactionsRequestDTO.isSaved()) {
                Account account = accountService.getAccountByAccountId(transactionsRequestDTO.getBeneficiaryAccount());
                Account userAccount = accountService.getAccountByAccountId(transactionsRequestDTO.getAccountId());
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

    @GetMapping("/{transaction_id}")
    public ResponseEntity<?> getTransactionStatus(@PathVariable("transaction_id") String transactionId){
        Map<String, Object> data = new LinkedHashMap<>();
        Transactions transaction;
        try {
            transaction = transactionService.getTransaction(transactionId);
            data.put("transaction_id", transaction.getTransactionId());
            data.put("account_id", transaction.getAccountId().getId());
            data.put("beneficiary_id", transaction.getBeneficiaryAccount().getId());
            data.put("amount", transaction.getAmount());
            data.put("date", transaction.getTransactionDate());
            data.put("note", transaction.getNote());
            data.put("admin_fee", transaction.getAdminFee());
            data.put("total", transaction.getAdminFee() + transaction.getAmount());
            return ResponseEntity.ok(BaseResponse.success(data, "Transaction detail retrieved"));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(BaseResponse.failure(404, "transaction not found"));
        }
    }

    @PostMapping("/history/{user_id}")
    public ResponseEntity<Map<String, Object>> getHistoriesByDate(
            @PathVariable("user_id") UUID userId, 
            @RequestBody(required = false) DateFilterRequestDTO request){

        Map<String, Object> response = new HashMap<>();
        List<TransactionsHistoryDTO> histories = transactionService.getTransactionByDate(userId, request);
        response.put("status", "success");
        if (histories.isEmpty()) {
            response.put("data", null);
            response.put("message", "No Transactions");
        }else{
            response.put("data", histories);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
