package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.QrisMerchantDTO;
import com.example.jangkau.models.Account;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.models.Merchant;
import com.example.jangkau.dto.MerchantRequestDTO;
import com.example.jangkau.repositories.MerchantRepository;
import com.example.jangkau.services.QrisService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
public class QrisServiceImpl implements QrisService {

    @Autowired AccountRepository accountRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    private static final String SECRET_KEY = "mySecretKey12345";
    private String initVector = "1234567812345678"; 

    @Override
    public String encrypString(QrisMerchantDTO qrisMerchantDTO) {
        UUID id;
        String owner;
        String number;
        try {
            Merchant merchant = merchantRepository.findById(qrisMerchantDTO.getId())
                .orElse(null);
            Account account = accountRepository.findById(qrisMerchantDTO.getId())
                .orElse(null);
            if (merchant != null) {
                id = merchant.getAccount().getId();
                owner = merchant.getName();
                number = merchant.getAccount().getAccountNumber();
            }else if(account != null){
                id = account.getId();
                owner = account.getOwnerName();
                number = account.getAccountNumber();
            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect ID");
            }
            String text = id+ "," + owner+ "," + number;
            String encryptedString = encrypt(text);
            return encryptedString;
        } catch (ResponseStatusException e) {
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
