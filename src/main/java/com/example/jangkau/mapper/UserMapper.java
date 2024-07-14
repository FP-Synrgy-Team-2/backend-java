package com.example.jangkau.mapper;

import com.example.jangkau.dto.UserResponse;
import com.example.jangkau.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .emailAddress(user.getEmailAddress())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
