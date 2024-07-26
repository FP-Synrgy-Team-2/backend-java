package com.example.jangkau.controllers;

import com.example.jangkau.config.EmailSender;
import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.mapper.SavedAccountMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;
import com.example.jangkau.serviceimpl.SavedAccountServiceImpl;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.SavedAccountService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/saved-accounts")
public class SavedAccountsController {
    @Autowired SavedAccountService savedAccountService;

    @Autowired ModelMapper modelMapper;

    @Autowired SavedAccountMapper savedAccountMapper;
   

    @GetMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> getSavedAccounts(@PathVariable("user_id") UUID userId){
        Map<String, Object> response = new HashMap<>();
        List<SavedAccounts> savedAccounts = savedAccountService.getAllSavedAccount(userId);
        List<AccountResponse> savedAccountsList = savedAccounts
                .stream()
                .map(account -> savedAccountMapper.toSavedAccountResponse(account))
                .collect(Collectors.toList());
        
        response.put("status", "success");
        if (savedAccountsList.isEmpty()) {
            response.put("data", null);
            response.put("message", "Nothing saved");
        }else{
            response.put("data", savedAccountsList);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
