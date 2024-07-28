package com.example.jangkau.mapper;

import com.example.jangkau.dto.auth.LoginResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class AuthMapper {
    private final AccountRepository accountRepository;

    public AuthMapper(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public LoginResponse toLoginResponse(ResponseEntity<Map> response, User user) {
        Optional<Account> accountOptional = accountRepository.findByUser(user);
        String accountNumber = accountOptional.map(Account::getAccountNumber).orElse(null);

        return LoginResponse.builder()
                .userId(user.getId())
                .accountNumber(accountNumber)
                .accessToken(response.getBody().get("access_token"))
                .tokenType(response.getBody().get("token_type"))
                .refreshToken(response.getBody().get("refresh_token"))
                .expiresIn(response.getBody().get("expires_in"))
                .scope(response.getBody().get("scope"))
                .jti(response.getBody().get("jti"))
                .build();
    }
}
