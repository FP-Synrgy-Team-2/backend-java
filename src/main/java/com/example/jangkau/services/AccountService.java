package com.example.jangkau.services;

import com.example.jangkau.models.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();

    Account getAccountById(String id);

    Account getAccountByAccountNumber(String accountNumber);
}
