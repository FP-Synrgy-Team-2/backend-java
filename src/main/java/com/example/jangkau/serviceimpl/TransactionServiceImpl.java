package com.example.jangkau.serviceimpl;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired TransactionRepository transactionRepository;
    @Autowired ModelMapper modelMapper;
    @Autowired AccountRepository accountRepository;
    

    @Override
    public List<Transactions> getAllTransactions(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTransactions'");
    }

    @Transactional
    @Override
    public TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO) {
        try {
            Account account_id = accountRepository.getById(transactionsRequestDTO.getAccount_id());
            Account beneficiary_account = accountRepository.getById(transactionsRequestDTO.getBeneficiary_account());
            Transactions newTransaction = Transactions.builder()
                .account(account_id)
                .beneficiary(beneficiary_account)
                .amount(transactionsRequestDTO.getAmount())
                .transaction_date(transactionsRequestDTO.getTransaction_date())
                .note(transactionsRequestDTO.getNote())
                .is_saved(transactionsRequestDTO.is_saved())
                .build();
            transactionRepository.save(newTransaction);
            newTransaction.setTransaction_id(newTransaction.getTransaction_id());
            return modelMapper.map(newTransaction, TransactionsResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Transactions getTransaction(String transaction_id) {
        UUID uuid = UUID.fromString(transaction_id);
        Transactions transaction = transactionRepository.findById(uuid).orElse(null);
        if (transaction != null) return transaction;
        else throw new RuntimeException("transaction not found");
    }
}
