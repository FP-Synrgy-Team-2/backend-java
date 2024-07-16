package com.example.jangkau.services;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<Account> getAllAccounts();

    Account getAccountById(String id);

    Account getAccountByAccountNumber(String accountNumber);

    List<Account> getSavedAccount(UUID user_id);

    void updateBalance(Transactions transactions);

    void createAccount(User user, String ownerName, int pin, @Nullable Double balance);
}
