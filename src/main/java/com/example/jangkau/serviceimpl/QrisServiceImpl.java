package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.QrisMerchantDTO;
import com.example.jangkau.dto.UserRequest;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.mapper.UserMapper;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.services.QrisService;
import com.example.jangkau.services.UserService;
import com.example.jangkau.services.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class QrisServiceImpl implements QrisService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserResponse generateQrCode(QrisMerchantDTO qrisMerchantDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateQrCode'");
    }

    
}
