package com.example.jangkau.controllers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.example.jangkau.dto.auth.QrisRequest;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.SavedAccounts;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.jangkau.dto.DateFilterRequestDTO;
import com.example.jangkau.dto.SavedAccountRequestDTO;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.TransactionsHistoryDTO;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.mapper.TransactionMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.services.AccountService;
import com.example.jangkau.services.SavedAccountService;
import com.example.jangkau.services.TransactionService;
import com.example.jangkau.services.UserService;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired TransactionService transactionService;
    @Autowired AccountService accountService;
    @Autowired ModelMapper modelMapper;
    @Autowired SavedAccountService savedAccountService;
    @Autowired UserService userService;
    @Autowired TransactionMapper transactionMapper;



    @PostMapping()
    public ResponseEntity<Map<String, Object>> createNewTransaction(@RequestBody TransactionsRequestDTO transactionsRequestDTO){
        Map<String, Object> response = new HashMap<>();
        TransactionsResponseDTO newTransaction = transactionService.createTransaction(transactionsRequestDTO);
        if (newTransaction != null) {
            Account accountId = accountService.getAccountByAccountId(newTransaction.getTo().getAccountId());
            Account beneficiary = accountService.getAccountByAccountId(newTransaction.getFrom().getAccountId());


            Transactions trans = modelMapper.map(newTransaction, Transactions.class);
            trans.setAccountId(accountId);
            trans.setBeneficiaryAccount(beneficiary);
            accountService.updateBalance(trans);
            if (transactionsRequestDTO.isSaved()) {
                Account account = accountService.getAccountByAccountId(transactionsRequestDTO.getBeneficiaryAccount());
                Account userAccount = accountService.getAccountByAccountId(transactionsRequestDTO.getAccountId());
                UserResponse user = userService.findById(userAccount.getUser().getId());
                SavedAccountRequestDTO request = SavedAccountRequestDTO.builder()
                    .accountId(account.getId())
                    .userId(user.getId())
                    .build();
                savedAccountService.createSavedAccount(request);
            }
        }
        response.put("status", "success");
        response.put("data", newTransaction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transaction_id}")
    public ResponseEntity<Map<String, Object>> getTransactionStatus(@PathVariable("transaction_id") String transactionId){
        Map<String, Object> response = new HashMap<>();
        Transactions transaction;
        try {
            transaction = transactionService.getTransaction(transactionId);
            TransactionsResponseDTO responseDTO = transactionMapper.toTransactionResponse(transaction);
            response.put("status", "transaction success");
            response.put("data", responseDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.put("status", e.getLocalizedMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/history/{user_id}")
    public ResponseEntity<Map<String, Object>> getHistoriesByDate(
            @PathVariable("user_id") String userId,
            @RequestBody(required = false) DateFilterRequestDTO request){

        Map<String, Object> response = new HashMap<>();
        List<TransactionsHistoryDTO> histories = transactionService.getTransactionByDate(userId, request);
        response.put("status", "success");
        if (histories.isEmpty()) {
            response.put("code", 404);
            response.put("data", null);
            response.put("message", "No Transactions");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("data", histories);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private static final String SECRET_KEY = "mySecretKey12345"; // Kunci enkripsi yang digunakan

    @PostMapping("/qrisTransaction")
    public ResponseEntity<?> qrisTransaction(@RequestBody QrisRequest qrisRequest) {
        try {
            // Log untuk melihat apakah QrisRequest sudah diterima dengan benar
            logger.info("Received QrisRequest: {}", qrisRequest);

            // Ambil data terenkripsi dari request
            String encryptedData = qrisRequest.getEncryptedData();
            if (encryptedData == null || encryptedData.isEmpty()) {
                logger.error("Encrypted data is null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data terenkripsi tidak boleh kosong");
            }

            logger.info("Encrypted Data Received: {}", encryptedData);

            // Dekripsi data terenkripsi
            String decryptedData = decrypt(encryptedData);
            logger.info("Decrypted Data: {}", decryptedData);

            // Asumsikan data terdekripsi dipisahkan dengan koma
            String[] accountData = decryptedData.split(",");
            logger.info("Account Data Length: {}", accountData.length);

            // Periksa apakah data lengkap
            if (accountData.length < 3) { // Pastikan ada 3 elemen: accountId, ownerName, accountNumber
                logger.error("Data tidak lengkap: {}", decryptedData);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("merchant tidak ditemukan");
            }

            // Buat objek AccountResponse dari data terdekripsi
            AccountResponse accountResponse = AccountResponse.builder()
                    .accountId(UUID.fromString(accountData[0]))
                    .ownerName(accountData[1])
                    .accountNumber(accountData[2])
                    .build();

            // Kembalikan respon dengan data akun
            return ResponseEntity.ok(accountResponse);

        } catch (Exception e) {
            logger.error("Kesalahan saat memproses data", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kesalahan saat memproses data");
        }
    }

    private String decrypt(String encryptedData) throws Exception {
        // Setup cipher untuk dekripsi
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Dekripsi data
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
    }
}
