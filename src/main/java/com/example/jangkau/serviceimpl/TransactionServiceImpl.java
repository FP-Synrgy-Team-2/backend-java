package com.example.jangkau.serviceimpl;

import java.time.ZonedDateTime;
import java.util.Date;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Calendar;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
import com.example.jangkau.services.AuthService;
import com.example.jangkau.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired TransactionRepository transactionRepository;
    @Autowired ModelMapper modelMapper;
    @Autowired AccountRepository accountRepository;
    @Autowired TransactionMapper transactionMapper;
    @Autowired UserRepository userRepository;
    @Autowired AuthService authService;

    @Transactional
    @Override
    public TransactionsResponseDTO createTransaction(TransactionsRequestDTO transactionsRequestDTO, Principal principal) {
        try {
            User user = authService.getCurrentUser(principal);

            Account account = accountRepository.findById(transactionsRequestDTO.getAccountId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            Account beneficiaryAccount = accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiary account not found"));

            if (account.getUser().getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            if (account.getId() == beneficiaryAccount.getId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot make transactions to the same bank account");
            }

            if (account.getBalance() < transactionsRequestDTO.getAmount()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            } else if (transactionsRequestDTO.getAmount() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
            }

            // Menggunakan ZonedDateTime untuk waktu transaksi
            ZonedDateTime transactionDate = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));

            Transactions newTransaction = Transactions.builder()
                    .accountId(account)
                    .beneficiaryAccount(beneficiaryAccount)
                    .amount(transactionsRequestDTO.getAmount())
                    .transactionDate(Date.from(transactionDate.toInstant()))  // Konversi ke Date
                    .note(transactionsRequestDTO.getNote())
                    .isSaved(transactionsRequestDTO.isSaved())
                    .transactionType("TRANSFER")
                    .build();

            transactionRepository.save(newTransaction);

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
    public List<TransactionsHistoryDTO> getTransactionByDate(String userId, DateFilterRequestDTO requestDTO, Principal principal) {
        try {
            UUID uuid = UUID.fromString(userId);
            User currentUser = authService.getCurrentUser(principal);
            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

            if (user.getId() != currentUser.getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            Account account = accountRepository.findByUser(user)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
            List<Transactions> transactions;

            // Pastikan waktu awal dan akhir menggunakan zonedDateTime Asia/Jakarta
            if (requestDTO != null) {
                ZonedDateTime startDate = requestDTO.getStartDate().toInstant().atZone(ZoneId.of("Asia/Jakarta"));
                ZonedDateTime endDate = requestDTO.getEndDate().toInstant().atZone(ZoneId.of("Asia/Jakarta")).plusDays(1).minusNanos(1);

                if (requestDTO.getStartDate().equals(requestDTO.getEndDate())) {
                    transactions = transactionRepository.findNowTransactions(account.getId(), Date.from(startDate.toInstant()));
                } else {
                    transactions = transactionRepository.findAllTransactionsByDate(account.getId(), Date.from(startDate.toInstant()), Date.from(endDate.toInstant()));
                }
            } else {
                transactions = transactionRepository.findAllTransactions(account.getId());
            }

            List<TransactionsHistoryDTO> histories = transactions
                    .stream()
                    .map(transaction -> transactionMapper.toTransactionsHistory(transaction, account.getId()))
                    .collect(Collectors.toList());
            return histories;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }


    @Override
    public List<TransactionsHistoryDTO> getAllTransaction(int page, String userId, Principal principal) {
        UUID uuid = UUID.fromString(userId);
        User currentUser = authService.getCurrentUser(principal);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
                
        if (user.getId() != currentUser.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must greater than 0");
        }
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

        Pageable pageable = PageRequest.of(page-1, 5, Sort.by("transactionDate").descending());
        List<Transactions> transactions = transactionRepository.findByAccountIdOrBeneficiaryAccount(account, account, pageable);
        List<TransactionsHistoryDTO> histories = transactions
                    .stream()
                    .map(transaction -> transactionMapper.toTransactionsHistory(transaction, account.getId()))
                    .collect(Collectors.toList());
            return histories;

    }

    
}
