package com.example.jangkau.transaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.serviceimpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    private TransactionsRequestDTO transactionsRequestDTO;
    private Principal principal;
    private User user;
    private Account account;
    private Account beneficiaryAccount;
    private Transactions transaction;

    @BeforeEach
    void setUp() {
        transactionsRequestDTO = new TransactionsRequestDTO();
        transactionsRequestDTO.setAccountId(UUID.randomUUID());
        transactionsRequestDTO.setBeneficiaryAccount(UUID.randomUUID());
        transactionsRequestDTO.setAmount(1000);
        transactionsRequestDTO.setNote("Test Note");
        transactionsRequestDTO.setSaved(false);

        principal = mock(Principal.class);
        user = new User();
        user.setId(UUID.randomUUID());

        account = new Account();
        account.setId(transactionsRequestDTO.getAccountId());
        account.setUser(user);
        account.setBalance(2000.0);

        beneficiaryAccount = new Account();
        beneficiaryAccount.setId(transactionsRequestDTO.getBeneficiaryAccount());

        transaction = new Transactions();
    }

    @Test
    void testCreateTransaction_Success() {
        when(authService.getCurrentUser(principal)).thenReturn(user);
        when(accountRepository.findById(transactionsRequestDTO.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())).thenReturn(Optional.of(beneficiaryAccount));
        when(transactionRepository.save(any(Transactions.class))).thenReturn(transaction);

        AccountResponse fromAccountResponse = AccountResponse.builder()
                .accountId(account.getId())
                .ownerName("Owner Name")
                .accountNumber("123456789")
                .build();

        AccountResponse toAccountResponse = AccountResponse.builder()
                .accountId(beneficiaryAccount.getId())
                .ownerName("Beneficiary Name")
                .accountNumber("987654321")
                .build();

        TransactionsResponseDTO responseDTO = TransactionsResponseDTO.builder()
                .transactionId(UUID.randomUUID())
                .from(fromAccountResponse)
                .to(toAccountResponse)
                .amount(transactionsRequestDTO.getAmount())
                .note(transactionsRequestDTO.getNote())
                .adminFee(0)
                .total(transactionsRequestDTO.getAmount())
                .transactionalType("TRANSFER")
                .build();

        when(transactionMapper.toTransactionResponse(any(Transactions.class))).thenReturn(responseDTO);

        TransactionsResponseDTO response = transactionServiceImpl.createTransaction(transactionsRequestDTO, principal);

        assertNotNull(response);
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    void testCreateTransaction_AccountNotFound() {
        when(authService.getCurrentUser(principal)).thenReturn(user);
        when(accountRepository.findById(transactionsRequestDTO.getAccountId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.createTransaction(transactionsRequestDTO, principal);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Account not found", exception.getReason());
    }

    @Test
    void testCreateTransaction_InsufficientBalance() {
        when(authService.getCurrentUser(principal)).thenReturn(user);
        when(accountRepository.findById(transactionsRequestDTO.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())).thenReturn(Optional.of(beneficiaryAccount));

        account.setBalance(50.00);

        assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.createTransaction(transactionsRequestDTO, principal);
        });

        verify(transactionRepository, never()).save(any(Transactions.class));
    }


    @Test
    void testCreateTransaction_SameAccountTransaction() {
        when(authService.getCurrentUser(principal)).thenReturn(user);
        when(accountRepository.findById(transactionsRequestDTO.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())).thenReturn(Optional.of(account));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.createTransaction(transactionsRequestDTO, principal);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Cannot make transactions to the same bank account", exception.getReason());
    }

    @Test
    void testCreateTransaction_AmountLessThanOrEqualToZero() {
        transactionsRequestDTO.setAmount(0);

        when(authService.getCurrentUser(principal)).thenReturn(user);
        when(accountRepository.findById(transactionsRequestDTO.getAccountId())).thenReturn(Optional.of(account));
        when(accountRepository.findById(transactionsRequestDTO.getBeneficiaryAccount())).thenReturn(Optional.of(beneficiaryAccount));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionServiceImpl.createTransaction(transactionsRequestDTO, principal);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Amount must be greater than 0", exception.getReason());
    }

}
