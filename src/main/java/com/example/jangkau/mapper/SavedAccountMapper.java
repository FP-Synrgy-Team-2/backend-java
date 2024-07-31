package com.example.jangkau.mapper;

import org.springframework.stereotype.Component;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.models.SavedAccounts;

@Component
public class SavedAccountMapper {
    public AccountResponse toSavedAccountResponse(SavedAccounts savedAccounts){
        return AccountResponse.builder()
            .accountId(savedAccounts.getAccount().getId())
            .accountNumber(savedAccounts.getAccount().getAccountNumber())
            .ownerName(savedAccounts.getAccount().getOwnerName())
            .build();
    }
}

    
