package com.example.jangkau.mapper;

import org.springframework.stereotype.Component;

import com.example.jangkau.dto.SavedAccountResponseDTO;
import com.example.jangkau.models.SavedAccounts;

@Component
public class SavedAccountMapper {

    public SavedAccountResponseDTO toSavedAccountResponse(SavedAccounts savedAccounts){
        return SavedAccountResponseDTO.builder()
            .id(savedAccounts.getId())
            .ownerName(savedAccounts.getAccount().getOwnerName())
            .accountNumber(savedAccounts.getAccount().getAccountNumber())
            .build();
    }

    

}
