package com.example.jangkau.mapper;

import org.springframework.stereotype.Component;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.models.SavedAccounts;

@Component
public class SavedAccountMapper {

    public AccountResponse toSavedAccountResponse(SavedAccounts savedAccounts){
        return AccountResponse.builder()
            .accountId(savedAccounts.getId())
            .ownerName(savedAccounts.getAccount().getOwnerName())
            .accountNumber(savedAccounts.getAccount().getAccountNumber())
            .build();
    }



}
