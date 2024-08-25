package com.example.jangkau.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID> {

    @Query(value = "SELECT * FROM transactions " +
            "WHERE (account_id = ?1 OR beneficiary_account = ?1) AND deleted_date IS NULL " +
            "ORDER BY transaction_date DESC", nativeQuery = true)
    List<Transactions> findAllTransactionsHistoryByAccount(UUID accountId);

    List<Transactions> findByBeneficiaryAccount(Account beneficiaryAccount);

    List<Transactions> findByAccountId(Account account);

    @Query(value = "SELECT * FROM transactions " +
            "WHERE (account_id = ?1 OR beneficiary_account = ?1) " +
            "AND transaction_date >= ?2 " +
            "AND transaction_date <= ?3 " +
            "AND deleted_date IS NULL " +
            "ORDER BY transaction_date DESC", nativeQuery = true)
    List<Transactions> findAllTransactionsByDate(UUID accountId, Date startDate, Date endDate);

    @Query(value = "SELECT * FROM transactions " +
            "WHERE (account_id = ?1 OR beneficiary_account = ?1) AND deleted_date IS NULL " +
            "ORDER BY transaction_date DESC", nativeQuery = true)
    List<Transactions> findAllTransactions(UUID accountId);

    @Query(value = "SELECT * FROM transactions " +
            "WHERE (account_id = ?1 OR beneficiary_account = ?1) " +
            "AND transaction_date = ?2 " +
            "AND deleted_date IS NULL " +
            "ORDER BY transaction_date DESC", nativeQuery = true)
    List<Transactions> findNowTransactions(UUID accountId, Date date);
}
