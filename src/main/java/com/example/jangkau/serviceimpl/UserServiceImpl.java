package com.example.jangkau.serviceimpl;

import com.example.jangkau.dto.UserRequest;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.mapper.UserMapper;
import com.example.jangkau.models.User;
import com.example.jangkau.repositories.UserRepository;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserResponse create(UserRequest userRequest) {
        validationService.validate(userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmailAddress(userRequest.getEmailAddress());
        user.setFullName(userRequest.getFullName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(encoder.encode(userRequest.getPassword()));

        if (userRepository.existsByEmailAddress(userRequest.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll(Pageable pageable, String username, String emailAddress) {
        Specification<User> spec = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !username.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }
            if (emailAddress != null && !emailAddress.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("emailAddress")), "%" + emailAddress.toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        List<UserResponse> response = new ArrayList<UserResponse>();
        userRepository.findAll(spec, pageable).forEach(user -> {
            log.info("USER : {}", user);
            response.add(userMapper.toUserResponse(user));
        });
        return response;
    }

    @Override
    public UserResponse update(Principal principal, UserRequest request) {
        validationService.validate(request);
        log.info("REQUEST : {}", request);
        User user = userRepository.findByUsername(principal.getName());

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (Objects.nonNull(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if (Objects.nonNull(request.getEmailAddress())) {
            user.setEmailAddress(request.getEmailAddress());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));
        userRepository.delete(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID User not found"));
        return userMapper.toUserResponse(user);
    }
}
