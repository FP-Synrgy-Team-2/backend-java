package com.example.jangkau.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID>{

    @Query(value = "select t.beneficiary_account, aa.owner_name , aa.account_number \r\n" + //
                "from transactions t \r\n" + //
                "left join account a on t.account_id = a.account_id \r\n" + //
                "left join account aa on t.beneficiary_account = aa.account_id \r\n" + //
                "where t.account_id = ? and is_saved = true and deleted_date is null", nativeQuery = true)
    List<Account> findSavedAccounts(UUID account_id);
} 