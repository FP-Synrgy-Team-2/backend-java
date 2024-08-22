package com.example.jangkau.services;

import java.security.Principal;
import java.util.UUID;

import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.QrisRequestDTO;
import com.example.jangkau.dto.QrisResponseDTO;
import com.example.jangkau.dto.QrisTransactionRequest;
import com.example.jangkau.dto.TransactionsRequestDTO;
import com.example.jangkau.dto.TransactionsResponseDTO;

public interface QrisService {

    String encrypStringMerchant(QrisRequestDTO qrisMerchantDTO, Principal principal);
    String encrypStringUser(QrisRequestDTO qrisMerchantDTO, Principal principal);
    QrisResponseDTO decrypt(String encryptedData) throws Exception;
    String encrypt(String text) throws Exception;
    boolean validateQr(UUID accountId, String text);
    void addQrCode(UUID accountId, String text);
    void cleanData();
    TransactionsResponseDTO createTransaction(QrisTransactionRequest request, Principal principal);


}
