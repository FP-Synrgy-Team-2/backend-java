package com.example.jangkau.repositories;

import com.example.jangkau.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Boolean existsByEmailAddress(String email);

    Boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByEmailAddress(String email);

    User findByOtp(String otp);

}
