package com.example.jangkau.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jangkau.models.SavedAccounts;

@Repository
public interface SavedAccountRepository extends JpaRepository<SavedAccounts, UUID>{
    @Query(value = "select * \n"+
        "from savedaccount s left join account a on a.account_id = s.account_id\n"+
        "where s.user_id = ? and s.deleted_date is null", nativeQuery = true)
    List<SavedAccounts> findSavedAccountByUserId(UUID userId);
    SavedAccounts findSavedAccountsByUserIdAndAccountId(UUID userId, UUID accountId);

} 