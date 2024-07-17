package com.example.jangkau.controllers;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.services.AccountService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/saved-accounts")
public class SavedAccountsController {
    @Autowired
    AccountService accountService;

    @Autowired ModelMapper modelMapper;

    @GetMapping("/{account_id}")
    public ResponseEntity<List<AccountResponseDTO>> getSavedAccounts(@PathVariable("account_id") UUID account_id){
        List<Account> savedAccounts = accountService.getSavedAccount(account_id);
        List<AccountResponseDTO> savedAccountsList = savedAccounts
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(savedAccountsList, HttpStatus.OK);

    }
}
