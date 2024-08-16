package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.QrisMerchantDTO;
import com.example.jangkau.dto.UserRequest;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.mapper.UserMapper;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Merchant;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.MerchantRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.dto.AccountResponse;
import com.example.jangkau.dto.AccountResponseDTO;
import com.example.jangkau.dto.MerchantRequestDTO;
import com.example.jangkau.services.QrisService;
import com.example.jangkau.services.UserService;
import com.example.jangkau.services.ValidationService;
import com.google.api.client.util.Value;
import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.persistence.criteria.Predicate;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class QrisServiceImpl implements QrisService {

    @Autowired AccountRepository accountRepository;

    @Autowired MerchantRepository merchantRepository;

    private static final String SECRET_KEY = "mySecretKey12345";
    private String initVector = "1234567812345678"; 

    @Override
    public String encrypString(QrisMerchantDTO qrisMerchantDTO) {

        try {
            Merchant merchant = merchantRepository.findById(qrisMerchantDTO.getMerchantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Merchant Not Found"));
            String text = merchant.getAccount().getId()+ "," + merchant.getName()+ "," + merchant.getAccount().getAccountNumber();
            String encryptedString = encrypt(text);
            return encryptedString;
        } catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @Override
    public Merchant createMerchant(MerchantRequestDTO merchantRequestDTO) {
        try {
            Merchant existMerchant = merchantRepository.findByNameandAccountId(merchantRequestDTO.getName(), merchantRequestDTO.getAccountId())
                .orElse(null);
            Account account = accountRepository.findById(merchantRequestDTO.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account Not Found"));

            if (existMerchant != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Merchant Already Exist");
            }else{
                Merchant merchant = Merchant.builder()
                        .name(merchantRequestDTO.getName())
                        .account(account)
                        .build();
                merchantRepository.save(merchant);
                merchant.setId(merchant.getId());
                return merchant;
            }
        }catch (ResponseStatusException e) {
            throw e; 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        } 
    }

    @Override
    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
        
    }

    @Override
    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes());

        return  Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
