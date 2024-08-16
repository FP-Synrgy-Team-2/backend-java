package com.example.jangkau.services;

import com.example.jangkau.dto.MerchantRequestDTO;
import com.example.jangkau.dto.QrisMerchantDTO;
import com.example.jangkau.dto.UserRequest;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.models.Merchant;
import com.example.jangkau.models.User;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface QrisService {

    String generateQrCode(QrisMerchantDTO qrisMerchantDTO);
    Merchant createMerchant(MerchantRequestDTO MerchantRequestDTO);
    String decrypt(String encryptedData) throws Exception;
    String encrypt(byte[] text) throws Exception;

}
