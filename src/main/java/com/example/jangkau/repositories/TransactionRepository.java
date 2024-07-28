package com.example.jangkau.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.jangkau.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import com.example.jangkau.models.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID>{
    @Query(value = "select * from transactions\n" +
            "where (account_id = ?1 or beneficiary_account = ?1) and deleted_date is null\n" +
            "order by transaction_date desc", nativeQuery = true)
    List<Transactions> findAllTransactionsHistoryByAccount(UUID accountId);

    List<Transactions> findByBeneficiaryAccount(Account beneficiaryAccount);
    
    List<Transactions> findByAccountId(Account account);

    @Query(value = "select * from transactions\n"+
        "where (account_id = ?1 or beneficiary_account = ?1) and transaction_date between ?2 and ?3 and deleted_date is null\n"+
        "order by transaction_date DESC", nativeQuery = true)
    List<Transactions> findAllTransactionsByDate(UUID accountId, Date startDate, Date endDate);
    
    @Query(value = "select * from transactions\n"+
    "where (account_id = ?1 or beneficiary_account = ?1) and deleted_date is null\n"+
    "order by transaction_date DESC", nativeQuery = true)
    List<Transactions> findAllTransactions(UUID accountId);
} 