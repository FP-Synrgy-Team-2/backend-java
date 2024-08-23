package com.example.jangkau.services;


import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface SavedAccountService {
    List<SavedAccounts> getAllSavedAccount(UUID userId, Principal principal);
    AccountResponse createSavedAccount(SavedAccountRequestDTO savedAccountRequestDTO);
}
