package com.example.jangkau.services;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired TransactionRepository transactionRepository;

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Account getAccountById(String id) {
        UUID uuid = UUID.fromString(id);
        Account account = accountRepository.findById(uuid).orElse(null);
        return account;
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElse(null);
        return account;
    }

    @Override
    public List<Account> getSavedAccount(UUID user_id) {
        return transactionRepository.findSavedAccounts(user_id);
    }

    @Override
    public void updateBalance(Transactions transactions) {
        Account source = getAccountByAccountNumber(transactions.getAccount().getAccountNumber());
        Account beneficiary = getAccountByAccountNumber(transactions.getBeneficiary().getAccountNumber());

        source.setBalance(source.getBalance() - transactions.getAmount());
        beneficiary.setBalance(beneficiary.getBalance() + transactions.getAmount());
        
        accountRepository.save(source);
        accountRepository.save(beneficiary);
    }
}
