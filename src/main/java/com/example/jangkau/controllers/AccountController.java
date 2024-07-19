package com.example.jangkau.controllers;

import com.example.jangkau.dto.*;
import com.example.jangkau.dto.auth.EmailRequest;
import com.example.jangkau.dto.base.BaseResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import com.example.jangkau.serviceimpl.UserServiceImpl;
import com.example.jangkau.services.AccountService;

import com.example.jangkau.services.ValidPassword;
import com.example.jangkau.services.ValidationService;
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
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    ValidationService validationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllBankAccounts() {
        Map<String, Object> response = new HashMap<>();
        List<Account> accountList = accountService.getAllAccounts();
        response.put("message", "Accounts successfully retrieved.");
        response.put("data", accountList);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<Map<String, Object>> getBankAccount(@PathVariable("user_id") String id) {
        Map<String, Object> response = new HashMap<>();
        Account account = accountService.getAccountById(id);
        HttpStatus httpStatus;
        Map<String, Object> data = new HashMap<>();
        if (account != null) {
            response.put("message", "Account successfully retrieved.");
            data.put("account_id", account.getId());
            data.put("owner_name", account.getOwnerName());
            data.put("account_number", account.getAccountNumber());
            data.put("balance", account.getBalance());
            httpStatus = HttpStatus.OK;
        } else {
            response.put("message", "Account not found.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        response.put("data", data);
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/account/{account_number}")
    public ResponseEntity<Map<String, Object>> getBankAccountByNumber(@PathVariable("account_number") String accountNumber) {
        Map<String, Object> response = new HashMap<>();
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        HttpStatus httpStatus;
        Map<String, Object> data = new HashMap<>();
        if (account != null) {
            response.put("message", "Account successfully retrieved.");
            data.put("account_id", account.getId());
            data.put("owner_name", account.getOwnerName());
            data.put("account_number", account.getAccountNumber());
            data.put("balance", account.getBalance());
            httpStatus = HttpStatus.OK;
        } else {
            response.put("message", "Account not found.");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        response.put("data", data);
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/pin-validation")
    public ResponseEntity<?> pinValidation(@RequestBody PinValidationDTO pinValidationDTO) {
        return ResponseEntity.ok(BaseResponse.success(accountService.pinValidation(pinValidationDTO), "PIN Validation Success"));
    }

    @PostMapping()
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        String ownerName = createAccountRequest.getOwnerName();
        Integer pin = createAccountRequest.getPin();
        Double balance = createAccountRequest.getBalance();
        Map<String, Object> response = new HashMap<>();
        try {
            Account account = accountService.createAccount(createAccountRequest.getUsername(), createAccountRequest.getPassword(), ownerName, pin, balance);
            response.put("account_id", account.getId());
            response.put("owner_name", account.getOwnerName());
            response.put("account_number", account.getAccountNumber());
            response.put("balance", account.getBalance());
            return ResponseEntity.ok(BaseResponse.success(response, "Account successfully created"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(BaseResponse.failure(409, e.getMessage()));
        }
    }
    
}
