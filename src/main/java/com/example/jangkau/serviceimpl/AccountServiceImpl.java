package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.dto.CreateAccountResponse;
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
import com.example.jangkau.services.ValidPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
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
    public Account getAccountByUserId(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        return accountRepository.findByUser(user).orElse(null);
    }

    @Override
    public Account getAccountByAccountId(UUID id) {
        Account account = accountRepository.findById(id).orElse(null);
        return account;
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElse(null);
    }


    @Override
    public void updateBalance(Transactions transactions) {

        Account source = accountRepository.findByAccountNumber(transactions.getAccountId().getAccountNumber()).orElse(null);
        Account beneficiary = accountRepository.findByAccountNumber(transactions.getBeneficiaryAccount().getAccountNumber()).orElse(null);

        source.setBalance(source.getBalance() - (transactions.getAmount() + transactions.getAdminFee()));
        beneficiary.setBalance(beneficiary.getBalance() + transactions.getAmount());
        
        accountRepository.save(source);
        accountRepository.save(beneficiary);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public Account createAccount(String username, String password, String ownerName, Integer pin, @Nullable Double balance) {
        User user = userRepository.findByUsername(username);
        Account oldAccount = accountRepository.findByUser(user).orElse(null);
        if (null == user) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        if (null == oldAccount) {
            oldAccount = Account.builder()
                    .user(user)
                    .ownerName(ownerName)
                    .build();
            oldAccount.setPin(pin, passwordEncoder);
            if (balance != null) oldAccount.setBalance(balance);
            else oldAccount.setBalance(0.0);
            if (passwordEncoder.matches(password, user.getPassword())) {
                Account account = accountRepository.save(oldAccount);
                return account;
            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "password is incorrect");
        } else throw new RuntimeException("Account already exists, you cannot create another account");
    }

    @Override
    public AccountResponseDTO pinValidation(PinValidationDTO pinValidationDTO) {

        try {
            Account account = accountRepository.findByAccountNumber(pinValidationDTO.getAccountNumber())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            if (!(encoder.matches(pinValidationDTO.getPin().toString(), account.getPin()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect PIN");
            }

            AccountResponseDTO response = AccountResponseDTO.builder()
                .userId(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .ownerName(account.getOwnerName())
                .build();
            return response;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
        
    }
}
