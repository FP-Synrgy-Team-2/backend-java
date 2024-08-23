package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.QrisRequestDTO;
import com.example.jangkau.dto.QrisResponseDTO;
import com.example.jangkau.dto.QrisTransactionRequest;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;
import com.example.jangkau.mapper.TransactionMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.AccountQR;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.models.Merchant;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.MerchantRepository;
import com.example.jangkau.repositories.QRRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.services.AuthService;
import com.example.jangkau.services.QrisService;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class QrisServiceImpl implements QrisService {

    @Autowired AccountRepository accountRepository;
    @Autowired MerchantRepository merchantRepository;
    @Autowired QRRepository qrRepository;
    @Autowired AuthService authService;
    @Autowired TransactionRepository transactionRepository;
    @Autowired ModelMapper modelMapper;
    @Autowired TransactionMapper transactionMapper;
    @Autowired UserRepository userRepository;

    private static final String SECRET_KEY = "mySecretKey12345";

    @Override
    public String encrypStringMerchant(QrisRequestDTO qrisMerchantDTO, Principal principal) {
        try {
            User user = authService.getCurrentUser(principal);

            Merchant merchant = merchantRepository.findById(qrisMerchantDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Merchant not found"));

            if (merchant.getAccount().getUser().getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            String text = merchant.getAccount().getId()+ "," + merchant.getName()+ "," + merchant.getAccount().getAccountNumber() + "," + "Merchant";
            String encryptedString = encrypt(text);
            return encryptedString;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    public String encrypStringUser(QrisRequestDTO qrisMerchantDTO, Principal principal) {
        try {
            User user = authService.getCurrentUser(principal);


            Account account = accountRepository.findById(qrisMerchantDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));
                
            if (account.getUser().getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            String text = account.getId()+ "," + account.getOwnerName()+ "," + account.getAccountNumber() + "," + "User";
            String encryptedString = encrypt(text);
            return encryptedString;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @Override
    public QrisResponseDTO decrypt(String encryptedData) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] ivAndEncryptedBytes = Base64.getDecoder().decode(encryptedData);

            byte [] iv = new byte[16];
            System.arraycopy(ivAndEncryptedBytes, 0, iv, 0, iv.length);

            byte[] encryptedBytes = new byte[ivAndEncryptedBytes.length - iv.length];
            System.arraycopy(ivAndEncryptedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] originalBytes = cipher.doFinal(encryptedBytes);

            String data = new String(originalBytes);
            String[] accountData = data.split(",");

            QrisResponseDTO response = QrisResponseDTO.builder()
                .accountId(UUID.fromString(accountData[0]))
                .ownerName(accountData[1])
                .accountNumber(accountData[2])
                .type(accountData[3])
                .build();

            if (response.getType().equalsIgnoreCase("user")) {
                boolean validateQr = validateQr(response.getAccountId(), encryptedData);
                if (validateQr != true) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Not Found");
                }
            }
            return response;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
        
    }

    @Override
    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);


        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        byte[] ivAndEncryptedBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, ivAndEncryptedBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, iv.length, encryptedBytes.length);

        return  Base64.getEncoder().encodeToString(ivAndEncryptedBytes);
    }

    @Override
    public boolean validateQr(UUID accountId, String text) {
        try {
            LocalDateTime now = LocalDateTime.now();
            AccountQR existQr = qrRepository.findByAccountandQr(accountId, text).orElse(null);
            if (existQr != null) {
                if (now.isBefore(existQr.getExpiredTime())) {
                    return true;
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "QR Code has expired.");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid.");
            }
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @Override
    public void addQrCode(UUID accountId, String text) {
        try {
            Account account = accountRepository.findById(accountId)
                .orElse(null);
            AccountQR qr = qrRepository.findByAccountQr(account).orElse(null);
            if (qr != null) {
                qrRepository.delete(qr);
            }
            AccountQR newQr = AccountQR.builder()
                    .accountQr(account)
                    .qr(text)
                    .expiredTime(LocalDateTime.now().plus(3, ChronoUnit.MINUTES).plus(30, ChronoUnit.SECONDS))
                    .build();
            qrRepository.save(newQr);

        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
       
    }

    @Override
    public void cleanData() {
        qrRepository.deleteByexpiredTimeBefore(LocalDateTime.now());
    }

    @Transactional
    @Override
    public TransactionsResponseDTO createTransaction(QrisTransactionRequest request, Principal principal) {
        try {
            User user = authService.getCurrentUser(principal);

            Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found"));

            Account beneficiaryAccount = accountRepository.findById(request.getBeneficiaryAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Beneficiary account not found"));

            if (account.getUser().getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

            if (account.getId() == beneficiaryAccount.getId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot make transactions to the same bank account");
            }
            
            if (account.getBalance() < request.getAmount()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
            }else if (request.getAmount() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must be greater than 0");
            }

            Transactions newTransaction = Transactions.builder()
                .accountId(account)
                .beneficiaryAccount(beneficiaryAccount)
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate())
                .transactionType("QRIS")
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
}
