package com.example.jangkau.serviceimpl;

import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.DateFilterRequestDTO;
import com.example.jangkau.dto.TransactionsHistoryDTO;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.mapper.TransactionMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired TransactionRepository transactionRepository;
    @Autowired ModelMapper modelMapper;
    @Autowired AccountRepository accountRepository;
    @Autowired TransactionMapper transactionMapper;
    @Autowired UserRepository userRepository;
    

    @Transactional
    @Override
    public TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO) {
        try {
            Account account = accountRepository.findById(transactionsRequestDTO.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            Account beneficiaryAccount = accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiary account not found"));

            if (account.getId() == beneficiaryAccount.getId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot make transactions to the same bank account");
            }
            
            if (account.getBalance() < transactionsRequestDTO.getAmount()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }
            Transactions newTransaction = Transactions.builder()
                .accountId(account)
                .beneficiaryAccount(beneficiaryAccount)
                .amount(transactionsRequestDTO.getAmount())
                .transactionDate(transactionsRequestDTO.getTransactionDate())
                .note(transactionsRequestDTO.getNote())
                .isSaved(transactionsRequestDTO.isSaved())
                .build();
            transactionRepository.save(newTransaction);
            newTransaction.setTransactionId(newTransaction.getTransactionId());
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

    @Override
    public List<TransactionsHistoryDTO> getTransactionByDate(UUID userId, DateFilterRequestDTO requestDTO) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        
            Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
            List<Transactions> transactions;

            if (requestDTO == null) {
                transactions = transactionRepository.findAllTransactions(account.getId());
            }else{
                transactions = transactionRepository.findAllTransactionsByDate(account.getId(), requestDTO.getStartDate(), requestDTO.getEndDate());
            }
            List<TransactionsHistoryDTO> histories = transactions
                .stream()
                .map(transaction -> transactionMapper.toTransactionsHistory(transaction, account.getId()))
                .collect(Collectors.toList());
            return histories;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }
}
