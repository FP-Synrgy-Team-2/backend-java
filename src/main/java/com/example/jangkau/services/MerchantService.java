package com.example.jangkau.services;

import com.example.jangkau.dto.MerchantRequest;
import com.example.jangkau.dto.MerchantResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MerchantService {

    MerchantResponse create(MerchantRequest request);

    MerchantResponse findById(UUID id);
}
