package com.example.jangkau.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import com.example.jangkau.models.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, UUID>{

} 