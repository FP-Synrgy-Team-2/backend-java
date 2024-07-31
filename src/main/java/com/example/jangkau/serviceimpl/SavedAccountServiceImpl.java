package com.example.jangkau.serviceimpl;


import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.mapper.SavedAccountMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.SavedAccountRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.SavedAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Service
public class SavedAccountServiceImpl implements SavedAccountService {

    @Autowired SavedAccountRepository savedAccountRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired UserRepository userRepository;
    @Autowired SavedAccountMapper savedAccountMapper;

     private static final Logger logger = LoggerFactory.getLogger(SavedAccountServiceImpl.class);

    @Override
    public List<SavedAccounts> getAllSavedAccount(UUID userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found"));
            return savedAccountRepository.findSavedAccountByUserId(userId);
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @Override
    public AccountResponse createSavedAccount(SavedAccountRequestDTO savedAccountRequestDTO) {
        try {
            SavedAccounts savedAccounts = savedAccountRepository.findSavedAccountsByUserIdAndAccountId(savedAccountRequestDTO.getUserId(), savedAccountRequestDTO.getAccountId());
            
            Account account = accountRepository.findById(savedAccountRequestDTO.getAccountId())
                .orElse(null);
            if (account == null) {
                logger.error("Bank account with accountId {} not found", savedAccountRequestDTO.getAccountId());
            }
            
            User user = userRepository.findById(savedAccountRequestDTO.getUserId())
                .orElse(null);
            if (user == null) {
                logger.error("User with userId {} not found", savedAccountRequestDTO.getUserId());
            }

            if (savedAccounts != null) {
                logger.error("Saved account with userId {} and accountId {} already exists", savedAccountRequestDTO.getUserId(), savedAccountRequestDTO.getAccountId());
            }else{
                SavedAccounts newSavedAccounts = SavedAccounts.builder()
                .account(account)
                .user(user)
                .build();

                savedAccountRepository.save(newSavedAccounts);
                newSavedAccounts.setId(newSavedAccounts.getId());
                return savedAccountMapper.toSavedAccountResponse(newSavedAccounts);
            }
            return null;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    
}
