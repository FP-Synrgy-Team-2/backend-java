package com.example.jangkau.controllers;

import com.example.jangkau.dto.*;
import com.example.jangkau.dto.auth.EmailRequest;
import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import com.example.jangkau.services.AccountService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/bank-accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllBankAccounts() {
        Map<String, Object> response = new HashMap<>();
        List<Account> accountList = accountService.getAllAccounts();
        response.put("message", "Accounts successfully retrieved.");
        response.put("data", accountList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{user_id}")
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

    @GetMapping("/{account_number}")
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

    @PostMapping("/pin-validation")
    public ResponseEntity<?> pinValidation(@RequestBody PinValidationDTO pinValidationDTO) {
        return ResponseEntity.ok(BaseResponse.success(accountService.pinValidation(pinValidationDTO), "PIN Validation Success"));
    }

    @PostMapping()
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        User user = modelMapper.map(createAccountRequest.getUserRequest(), User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String ownerName = createAccountRequest.getOwnerName();
        Integer pin = createAccountRequest.getPin();
        Double balance = createAccountRequest.getBalance();
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("ownerName", ownerName);
        data.put("balance", balance);
        try {
            accountService.createAccount(user, ownerName, pin, balance);
            return ResponseEntity.ok(BaseResponse.success(data, "Account successfully created"));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(BaseResponse.failure(409, e.getMessage()));
        }
    }
    
}
