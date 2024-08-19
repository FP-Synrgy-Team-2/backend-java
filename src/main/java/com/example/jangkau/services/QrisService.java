package com.example.jangkau.services;

import com.example.jangkau.dto.QrisMerchantDTO;

public interface QrisService {

    String encrypString(QrisMerchantDTO qrisMerchantDTO);
    String decrypt(String encryptedData) throws Exception;
    String encrypt(String text) throws Exception;


}
