package com.example.jangkau.controllers;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.dto.TransactionInOutHistoryDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.serviceimpl.InOutServiceImpl;
import com.example.jangkau.services.InOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    InOutService inOutService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/{account_id}")
    public ResponseEntity<?> history(@PathVariable("account_id") String accountId) {
        Account account = accountRepository.findById(UUID.fromString(accountId)).orElse(null);
        if (account == null) return ResponseEntity.notFound().build();

        List<TransactionInOutHistoryDTO> transactionInOutHistoryDTOList = inOutService.getTransactionInOutHistory(account);
        Map<String, Object> data = new HashMap<>();
        data.put("account-activity", transactionInOutHistoryDTOList);
        return ResponseEntity.ok(data);
    }
}
