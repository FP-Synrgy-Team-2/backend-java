package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.dto.PinValidationDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;

import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.resources.DummyResource;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired TransactionRepository transactionRepository;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

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
    public List<Account> getSavedAccount(UUID account_id) {
        return transactionRepository.findSavedAccounts(account_id);
    }

    @Override
    public void updateBalance(Transactions transactions) {

        Account source = accountRepository.findByAccountNumber(transactions.getAccount_id().getAccountNumber()).orElse(null);
        Account beneficiary = accountRepository.findByAccountNumber(transactions.getBeneficiary_account().getAccountNumber()).orElse(null);

        source.setBalance(source.getBalance() - (transactions.getAmount() + transactions.getAdmin_fee()));
        beneficiary.setBalance(beneficiary.getBalance() + transactions.getAmount());
        
        accountRepository.save(source);
        accountRepository.save(beneficiary);
    }

    @Override
    public void createAccount(User user, String ownerName, Integer pin, @Nullable Double balance) {
        User oldUser = userRepository.findByUsername(user.getUsername());
        Account oldAccount = accountRepository.findByUser(oldUser).orElse(null);
        if (null == oldUser) {
            userRepository.save(user);
        }
        if (null == oldAccount) {
            oldAccount = Account.builder()
                    .user(user)
                    .ownerName(ownerName)
                    .build();
            oldAccount.setPin(pin, passwordEncoder);
            if (balance != null) oldAccount.setBalance(balance);
            else oldAccount.setBalance(0.0);
            accountRepository.save(oldAccount);
        } else throw new RuntimeException("Account already exists, you cannot create another account");
    }

    @Override
    public AccountResponseDTO pinValidation(PinValidationDTO pinValidationDTO) {

        try {
            Account account = accountRepository.findByAccountNumber(pinValidationDTO.getAccountNumber())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            if (!(encoder.matches(pinValidationDTO.getPin(), account.getPin()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect PIN");
            }

            AccountResponseDTO response = AccountResponseDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .owner_name(account.getOwnerName())
                .build();
            return response;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
        
    }
}
