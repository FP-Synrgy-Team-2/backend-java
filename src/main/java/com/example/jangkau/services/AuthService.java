package com.example.jangkau.services;

import com.example.jangkau.dto.auth.*;
import com.example.jangkau.models.User;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public interface AuthService {

    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request, HttpServletResponse response);

    Object sendEmailOtp(EmailRequest request, String subject);

    Object checkOtpValid(OtpRequest otp);

    Object resetPassword(ResetPasswordRequest request);

    User getCurrentUser(Principal principal);

}
