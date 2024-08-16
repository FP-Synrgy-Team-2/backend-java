package com.example.jangkau.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.Merchant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import com.example.jangkau.models.Transactions;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID>{
    @Query(value = "select * from merchant\n"+
    "where name = ?1 and account_id = ?2 and deleted_date is null", nativeQuery = true)
    Optional<Merchant> findByNameandAccountId(String name, UUID accountId);
} 