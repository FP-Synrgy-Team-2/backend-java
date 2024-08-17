package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.MerchantRequest;
import com.example.jangkau.dto.MerchantResponse;
import com.example.jangkau.mapper.MerchantMapper;
import com.example.jangkau.models.Merchant;
import com.example.jangkau.repositories.MerchantRepository;
import com.example.jangkau.services.MerchantService;
import com.example.jangkau.services.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private MerchantMapper merchantMapper;

    private MerchantResponse getMerchantResponse(MerchantRequest request, Merchant merchant) {
        merchant.setName(request.getName());
        merchantRepository.save(merchant);
        return merchantMapper.toMerchantResponse(merchant);
    }

    @Override
    public MerchantResponse create(MerchantRequest request) {
        validationService.validate(request);
        Merchant merchant = new Merchant();
        return getMerchantResponse(request, merchant);
    }

    @Override
    public MerchantResponse findById(UUID id) {
        Merchant merchant = merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Merchant not found"));
        return merchantMapper.toMerchantResponse(merchant);
    }

}
