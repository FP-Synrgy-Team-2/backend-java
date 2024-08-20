package com.example.jangkau.repositories;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.AccountQR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QRRepository extends JpaRepository<AccountQR, UUID> {
    Optional<AccountQR> findByAccountQr(Account accountId);

    @Query(value = "select * from account_qr where account_id =?1 and qr = ?2 and deleted_date is null", nativeQuery = true)
    Optional<AccountQR> findByAccountandQr(UUID accountId, String qr);

    void deleteByexpiredTimeBefore(LocalDateTime localDateTime);

}
