package com.example.jangkau.services;


import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.SavedAccountResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;

import java.util.List;
import java.util.UUID;

public interface SavedAccountService {
    List<SavedAccounts> getAllSavedAccount(UUID userId);
    SavedAccountResponseDTO createSavedAccount(SavedAccountRequestDTO savedAccountRequestDTO);
}
