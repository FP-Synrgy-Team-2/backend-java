package com.example.jangkau.repositories;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUser(User oldUser);

}
