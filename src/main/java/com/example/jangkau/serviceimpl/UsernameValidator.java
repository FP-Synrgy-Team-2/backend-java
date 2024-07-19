package com.example.jangkau.serviceimpl;

import com.example.jangkau.services.ValidUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Regex to ensure username contains letters, numbers, at least one uppercase letter, and no symbols
        String usernamePattern = "^(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d]+$";
        return username.matches(usernamePattern);
    }
}
