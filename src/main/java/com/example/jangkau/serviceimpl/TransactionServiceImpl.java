package com.example.jangkau.serviceimpl;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;


import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.mapper.TransactionMapper;
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
    @Autowired TransactionMapper transactionMapper;
    

    @Override
    public List<Transactions> getAllTransactions(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTransactions'");
    }

    @Transactional
    @Override
    public TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO) {
        try {
            Account account = accountRepository.findById(transactionsRequestDTO.getAccount_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            Account beneficiaryAccount = accountRepository.findById(transactionsRequestDTO.getBeneficiary_account())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiary account not found"));

            
            if (transactionsRequestDTO.getAmount() < 10000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum amount Rp. 10000");
            }else if (account.getBalance() == 50000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your balance has reached the limit");
            }else if (account.getBalance() - transactionsRequestDTO.getAmount() < 50000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }
            Transactions newTransaction = Transactions.builder()
                .account_id(account)
                .beneficiary_account(beneficiaryAccount)
                .amount(transactionsRequestDTO.getAmount())
                .transaction_date(transactionsRequestDTO.getTransaction_date())
                .note(transactionsRequestDTO.getNote())
                .is_saved(transactionsRequestDTO.is_saved())
                .build();
            transactionRepository.save(newTransaction);
            newTransaction.setTransaction_id(newTransaction.getTransaction_id());
            return transactionMapper.toTransactionResponse(newTransaction);
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
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
