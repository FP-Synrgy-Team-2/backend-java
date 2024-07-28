package com.example.jangkau.services;

import com.example.jangkau.dto.UserRequest;
import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.models.User;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    List<UserResponse> findAll(Pageable pageable, String username, String emailAddress);

    UserResponse update(Principal principal, UserRequest request);

    UserResponse delete(UUID id);

    UserResponse findById(UUID id);
}
