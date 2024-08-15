package com.example.jangkau.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.example.jangkau.mapper.TransactionMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.SavedAccountService;
import com.example.jangkau.services.TransactionService;
import com.example.jangkau.services.UserService;



@RestController
@RequestMapping("/transactions")
public class QrisController {

    @Autowired TransactionService transactionService;
    @Autowired AccountService accountService;
    @Autowired ModelMapper modelMapper;
    @Autowired SavedAccountService savedAccountService;
    @Autowired UserService userService;
    @Autowired TransactionMapper transactionMapper;


    

}
