package com.example.jangkau.controllers;

import com.example.jangkau.models.Account;
import com.example.jangkau.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/bank-accounts")
    public ResponseEntity<Map<String, Object>> getAllBankAccounts() {
        Map<String, Object> response = new HashMap<>();
        List<Account> accountList = accountService.getAllAccounts();
        response.put("message", "Accounts successfully retrieved.");
        response.put("data", accountList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/bank-accounts/{user_id}")
    public ResponseEntity<Map<String, Object>> getBankAccount(@PathVariable("user_id") String id) {
        Map<String, Object> response = new HashMap<>();
        Account account = accountService.getAccountById(id);
        HttpStatus httpStatus;
        if (account != null) {
            response.put("message", "Account successfully retrieved.");
            httpStatus = HttpStatus.OK;
        } else {
            response.put("message", "Account not found.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        response.put("data", account);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/bank-accounts/{account_number}")
    public ResponseEntity<Map<String, Object>> getBankAccountByNumber(@PathVariable("account_number") String accountNumber) {
        Map<String, Object> response = new HashMap<>();
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus;
        if (account != null) {
            response.put("message", "Account successfully retrieved.");
            httpStatus = HttpStatus.OK;
        } else {
            response.put("message", "Account not found.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        response.put("data", account);
        return new ResponseEntity<>(response, httpStatus);
    }
}
