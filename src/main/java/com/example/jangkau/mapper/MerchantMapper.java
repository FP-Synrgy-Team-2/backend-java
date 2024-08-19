package com.example.jangkau.mapper;

import com.example.jangkau.dto.MerchantResponse;
import com.example.jangkau.models.Account;
import com.example.jangkau.models.Merchant;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper {
    public MerchantResponse toMerchantResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .id(merchant.getId().toString())
                .name(merchant.getName())
                .accountId(merchant.getAccount() != null ? merchant.getAccount().getId().toString() : null)
                .build();
    }
}
